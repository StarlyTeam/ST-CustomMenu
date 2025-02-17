package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.data.GlobalAction;
import net.starly.custommenu.dispatcher.ChatInputDispatcher;
import net.starly.custommenu.inventory.holder.ActionTypeInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.inventory.page.PaginationManager;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GlobalActionDetailEditor extends InventoryListenerBase {

    private static GlobalActionDetailEditor instance;

    public static GlobalActionDetailEditor getInstance() {
        if (instance == null) instance = new GlobalActionDetailEditor();
        return instance;
    }

    private GlobalActionDetailEditor() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        Player player = (Player) event.getWhoClicked();
        if (inventory == player.getInventory()) return;

        ActionTypeInvHolder holder = (ActionTypeInvHolder) inventory.getHolder();
        Menu menu = parseMenu(inventory);
        int slot = event.getSlot();

        if (slot == 26) {
            player.closeInventory();
            try {
                player.playSound(player.getLocation(), Sound.valueOf("ITEM_BOOK_PAGE_TURN"), 1f, 1f);
            } catch (IllegalArgumentException ignored) {}
        } else if (slot == holder.getPrevBtnSlot()) {
            PaginationManager paginationManager = parsePaginationManager(inventory);
            if (paginationManager.getCurrentPage() > 1) {
                paginationManager.prevPage();

                unregisterListener(player.getUniqueId());
                player.closeInventory();
                openInventory(player, menu, holder.getActionType(), holder.getPaginationManager().getCurrentPage());

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        } else if (slot == holder.getNextBtnSlot()) {
            PaginationManager paginationManager = parsePaginationManager(inventory);
            if (paginationManager.getPages().size() > paginationManager.getCurrentPage()) {
                paginationManager.nextPage();

                unregisterListener(player.getUniqueId());
                player.closeInventory();
                openInventory(player, menu, holder.getActionType(), holder.getPaginationManager().getCurrentPage());

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        } else if (slot == 22) {
            parseGlobalAction(inventory).getArgs().add("<undefined>");

            unregisterListener(player.getUniqueId());
            player.closeInventory();
            openInventory(player, menu, holder.getActionType(), holder.getPaginationManager().getCurrentPage());
        } else if (!(slot >= 20 && slot <= 25) && event.getCurrentItem() != null) {
            int argIndex = (holder.getPaginationManager().getCurrentPage() - 1) * 18 + slot;
            GlobalAction action = menu.getGlobalAction(holder.getActionType());

            ClickType clickType = event.getClick();
            if (clickType == ClickType.LEFT) {
                MessageContent messageContent = MessageContent.getInstance();
                messageContent.getMessageAfterPrefix(MessageType.NORMAL, "enterNewArg")
                        .ifPresent(player::sendMessage);

                ChatInputDispatcher.attachConsumer(player.getUniqueId(), (chatEvent) -> {
                    String message = chatEvent.getMessage();
                    action.getArgs().set(argIndex, message);

                    messageContent.getMessageAfterPrefix(MessageType.NORMAL, "ArgumentSet_GLOBAL")
                            .map(value -> value
                                    .replace("{menu}", menu.getId())
                                    .replace("{actionType}", holder.getActionType())
                                    .replace("{argIndex}", String.valueOf(argIndex + 1))
                                    .replace("{value}", message))
                            .ifPresent(player::sendMessage);

                    openInventory(player, menu, holder.getActionType(), holder.getPaginationManager().getCurrentPage());
                });

                unregisterListener(player.getUniqueId());
                player.closeInventory();
            } else if (clickType == ClickType.RIGHT) {
                MessageContent messageContent = MessageContent.getInstance();
                messageContent.getMessageAfterPrefix(MessageType.NORMAL, "enterNewArgIndex")
                        .ifPresent(player::sendMessage);

                Consumer<AsyncPlayerChatEvent> listener = new Consumer<AsyncPlayerChatEvent>() {

                    @Override
                    public void accept(AsyncPlayerChatEvent event) {
                        String message = event.getMessage();
                        int newArgIndex;
                        try {
                            newArgIndex = Integer.parseInt(message);
                        } catch (NumberFormatException ignored) {
                            tryAgain();
                            return;
                        }

                        if (newArgIndex < 1) {
                            tryAgain();
                            return;
                        } else if (newArgIndex > action.getArgs().size()) {
                            tryAgain();
                            return;
                        }

                        action.getArgs().add(newArgIndex - 1, action.getArgs().get(argIndex));
                        action.getArgs().remove(argIndex);
                        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "ArgumentIndexSet_GLOBAL")
                                .map(value -> value
                                        .replace("{menu}", menu.getId())
                                        .replace("{actionType}", holder.getActionType())
                                        .replace("{argIndex}", String.valueOf(argIndex + 1))
                                        .replace("{value}", message))
                                .ifPresent(player::sendMessage);

                        openInventory(player, menu, action.getActionType(), holder.getPaginationManager().getCurrentPage());
                    }

                    private void tryAgain() {
                        messageContent.getMessageAfterPrefix(MessageType.ERROR, "invalidNumberInput")
                                .ifPresent(player::sendMessage);
                        ChatInputDispatcher.attachConsumer(player.getUniqueId(), this);
                    }
                };
                ChatInputDispatcher.attachConsumer(player.getUniqueId(), listener);

                unregisterListener(player.getUniqueId());
                player.closeInventory();
            } else if (clickType == ClickType.SHIFT_RIGHT) {
                action.getArgs().remove(argIndex);

                MessageContent messageContent = MessageContent.getInstance();
                messageContent.getMessageAfterPrefix(MessageType.NORMAL, "ArgumentRemoved_GLOBAL")
                        .map(value -> value
                                .replace("{menu}", menu.getId())
                                .replace("{actionType}", holder.getActionType())
                                .replace("{argIndex}", String.valueOf(argIndex + 1)))
                        .ifPresent(player::sendMessage);

                int currentPage = holder.getPaginationManager().getCurrentPage();
                if (inventory.getItem(1) == null && currentPage > 1) currentPage--;

                unregisterListener(player.getUniqueId());
                player.closeInventory();
                openInventory(player, menu, holder.getActionType(), currentPage);
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        ActionTypeInvHolder holder = (ActionTypeInvHolder) inventory.getHolder();

        JavaPlugin plugin = CustomMenu.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            MenuRepository menuRepository = MenuRepository.getInstance();

            Player player = (Player) event.getPlayer();
            Menu menu = menuRepository.getMenu(holder.getMenuId());
            MenuGlobalActionEditor.getInstance().openInventory(player, menu);
        }, 1L);
    }

    @Override
    @Deprecated
    public void openInventory(Player player, Menu menu) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void openInventory(Player player, Menu menu, String actionType, int page) {
        JavaPlugin plugin = CustomMenu.getInstance();


        List<String> args = menu.getGlobalAction(actionType).getArgs();
        List<ItemStack> argItemList = new ArrayList<>();

        for (int i = 0; i < args.size(); i++) {
            String argument = args.get(i);

            ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r§7" + (i + 1) + ". §r§e" + argument);
            itemMeta.setLore(Arrays.asList(
                    "§r§e• §7인덱스 : §6" + (i + 1),
                    "",
                    "§r§e• §6좌클릭 §7: §a§n편집",
                    "§r§e• §6우클릭 §7: §a§n인덱스 수정",
                    "§r§e• §6Shift+우클릭 §7: §c§n삭제"
            ));
            itemStack.setItemMeta(itemMeta);

            argItemList.add(itemStack);
        }

        PaginationManager paginationManager = new PaginationManager(argItemList, 18);
        paginationManager.setCurrentPage(page);
        ActionTypeInvHolder holder = new ActionTypeInvHolder(paginationManager, 18 ,19, menu.getId(), actionType);
        Inventory inventory = plugin.getServer().createInventory(holder, 27, menu.getTitle() + "§r [액션 세부::" + page + "]");

        {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("BLACK_STAINED_GLASS_PANE"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 15);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r");
            itemStack.setItemMeta(itemMeta);

            for (int i = 20; i <= 21; i++) inventory.setItem(i, itemStack);
            for (int i = 23; i <= 24; i++) inventory.setItem(i, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§a액션인자 추가");
            itemMeta.setLore(Arrays.asList("§r§e• §7액션인자를 추가합니다."));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(22, itemStack);
        } {
            Material material;
            try {
                material = Material.valueOf("EMPTY_MAP");
            } catch (IllegalArgumentException ignored) {
                material = Material.valueOf("MAP");
            }

            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6§l정보");
            itemMeta.setLore(Arrays.asList(
                    "§r§e• §7액션타입 : §6" + actionType,
                    "§r§e• §7액션인자 : §6" + String.join("§7, §6", args)
            ));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(25, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c§l§n뒤로가기");
            itemMeta.setLore(Arrays.asList("§r§e• §7뒤로 돌아갑니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(26, itemStack);
        }

        openInventoryAndRegisterListener(player, holder.getInventory(inventory));
    }
}
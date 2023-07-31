package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.data.Action;
import net.starly.custommenu.action.expansion.IActionExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.menu.button.MenuButton;
import net.starly.custommenu.dispatcher.ChatInputDispatcher;
import net.starly.custommenu.inventory.holder.impl.ButtonInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.inventory.page.PaginationManager;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
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
import java.util.stream.Collectors;

public class ButtonActionEditor extends InventoryListenerBase {

    private static ButtonActionEditor instance;

    public static ButtonActionEditor getInstance() {
        if (instance == null) instance = new ButtonActionEditor();
        return instance;
    }

    private ButtonActionEditor() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        Player player = (Player) event.getWhoClicked();
        if (inventory == player.getInventory()) return;

        PaginationManager paginationManager = parsePaginationManager(inventory);
        ButtonInvHolder holder = (ButtonInvHolder) inventory.getHolder();
        Menu menu = parseMenu(inventory);
        int slot = event.getSlot();

        if (slot == 49) {
            player.performCommand("st-custommenu:메뉴 애드온 목록");

            MessageContent messageContent = MessageContent.getInstance();
            messageContent.getMessageAfterPrefix(MessageType.NORMAL, "enterActionType")
                    .ifPresent(player::sendMessage);

            Consumer<AsyncPlayerChatEvent> listener = new Consumer<AsyncPlayerChatEvent>() {

                @Override
                public void accept(AsyncPlayerChatEvent chatEvent) {
                    String message = chatEvent.getMessage();

                    ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
                    ActionExpansion expansion = expansionRegistry.getExpansion(message);
                    if (expansion == null) {
                        tryAgain();
                        return;
                    }

                    int buttonSlot = holder.getSlot();
                    List<Action> actionList = menu.getButton(buttonSlot).getActionList();
                    actionList.add(expansion.parseAction(new ArrayList<>()));

                    messageContent.getMessageAfterPrefix(MessageType.NORMAL, "ActionAdded_GENERAL")
                            .map(value -> value
                                    .replace("{menu}", menu.getId())
                                    .replace("{slot}", String.valueOf(buttonSlot + 1))
                                    .replace("{actionType}", message))
                            .ifPresent(player::sendMessage);

                    ButtonActionEditor.getInstance().openInventory(player, menu, buttonSlot, paginationManager.getCurrentPage());
                }

                private void tryAgain() {
                    messageContent.getMessageAfterPrefix(MessageType.ERROR, "invalidActionType")
                            .ifPresent(player::sendMessage);
                    ChatInputDispatcher.attachConsumer(player.getUniqueId(), this);
                }
            };
            ChatInputDispatcher.attachConsumer(player.getUniqueId(), listener);

            unregisterListener(player.getUniqueId());
            player.closeInventory();
        } else if (slot == 53) {
            player.closeInventory();
            try {
                player.playSound(player.getLocation(), Sound.valueOf("ITEM_BOOK_PAGE_TURN"), 1f, 1f);
            } catch (IllegalArgumentException ignored) {}
        } else if (slot == holder.getPrevBtnSlot()) {
            if (paginationManager.getCurrentPage() > 1) {
                paginationManager.prevPage();

                unregisterListener(player.getUniqueId());
                openInventory(player, menu, slot, paginationManager.getCurrentPage());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        } else if (slot == holder.getNextBtnSlot()) {
            if (paginationManager.getPages().size() > paginationManager.getCurrentPage()) {
                paginationManager.nextPage();

                unregisterListener(player.getUniqueId());
                openInventory(player, menu, slot, paginationManager.getCurrentPage());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            }
        } else if (!(47 <= slot && slot <= 52) && event.getCurrentItem() != null) {
            int actionIndex = (paginationManager.getCurrentPage() - 1) * 45 + slot;

            MessageContent messageContent = MessageContent.getInstance();
            MenuButton button = menu.getButton(holder.getSlot());
            Action action = button.getActionList().get(actionIndex);
            ActionExpansion expansion = action.getExpansion();

            String prefix = messageContent.getPrefix().orElse("");
            expansion.getDescriptionLore().forEach(message -> player.sendMessage(prefix + message));
            messageContent.getMessageAfterPrefix(MessageType.NORMAL, "enterArgs")
                    .ifPresent(player::sendMessage);

            if (event.getClick() == ClickType.LEFT) {
                ChatInputDispatcher.attachConsumer(player.getUniqueId(), (chatEvent) -> {
                    String message = chatEvent.getMessage();

                    List<String> parsedArgs = Arrays.asList(message.split(" "));
                    List<String> args = action.getArgs();
                    args.clear();
                    args.addAll(parsedArgs);


                    messageContent.getMessageAfterPrefix(MessageType.NORMAL, "ArgumentSet_GENERAL")
                            .map(value -> value
                                    .replace("{menu}", menu.getId())
                                    .replace("{slot}", String.valueOf(holder.getSlot() + 1))
                                    .replace("{actionIndex}", String.valueOf(actionIndex))
                                    .replace("{actionType}", action.getActionType())
                                    .replace("{value}", message))
                            .ifPresent(player::sendMessage);

                    ButtonActionEditor.getInstance().openInventory(player, menu, holder.getSlot(), paginationManager.getCurrentPage());
                });

                unregisterListener(player.getUniqueId());
                player.closeInventory();
            } else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                button.getActionList().remove(actionIndex);

                unregisterListener(player.getUniqueId());
                player.closeInventory();
                openInventory(player, menu, holder.getSlot(), paginationManager.getCurrentPage());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = parseMenu(event.getInventory());

        JavaPlugin plugin = CustomMenu.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> MenuButtonEditor.getInstance().openInventory(player, menu), 1L);
    }

    @Override
    @Deprecated
    public void openInventory(Player player, Menu menu) {
        throw new UnsupportedOperationException("사용할 수 없는 메소드가 호출되었습니다.");
    }

    public void openInventory(Player player, Menu menu, int slot, int page) {
        JavaPlugin plugin = CustomMenu.getInstance();

        List<Action> actionList = menu.getButton(slot).getActionList();
        List<ItemStack> actionItemList = new ArrayList<>();

        for (int index = 0; index < actionList.size(); index++) {
            Action action = actionList.get(index);
            ActionExpansion expansion = ActionExpansionRegistry.getInstance().getExpansion(action.getActionType());

            ItemStack itemStack = new ItemStack(expansion.getItemType());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r§7" + (index + 1) + ". §r§e" + action.getActionType());
            List<String> lore = expansion.getDescriptionLore().stream()
                    .map(line -> "§r§e• §f" + line).collect(Collectors.toList());
            lore.addAll(Arrays.asList(
                    "",
                    "§r§e• §7액션타입 : §6" + action.getActionType(),
                    "§r§e• §7액션인자 : §6" + String.join(" ", action.getArgs()),
                    "",
                    "§r§e• §6좌클릭 §7: §a§n액션인자 설정",
                    "§r§e• §6Shift+우클릭 §7: §c§n삭제"
            ));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            actionItemList.add(itemStack);
        }

        PaginationManager paginationManager = new PaginationManager(actionItemList, 45);
        paginationManager.setCurrentPage(page);
        ButtonInvHolder holder = new ButtonInvHolder(paginationManager, 45, 46, menu.getId(), slot);
        Inventory inventory = plugin.getServer().createInventory(holder, 54, menu.getTitle() + "§r [버튼 액션::" + page + "]");

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

            for (int i = 47; i <= 48; i++) inventory.setItem(i, itemStack);
            for (int i = 50; i <= 52; i++) inventory.setItem(i, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§a액션 추가");
            itemMeta.setLore(Arrays.asList("§r§e• §7액션을 생성합니다."));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(49, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c§l§n뒤로가기");
            itemMeta.setLore(Arrays.asList("§r§e• §7뒤로 돌아갑니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(53, itemStack);
        }

        openInventoryAndRegisterListener(player, holder.getInventory(inventory));
    }
}

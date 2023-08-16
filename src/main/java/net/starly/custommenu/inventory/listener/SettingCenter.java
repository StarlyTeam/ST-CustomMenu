package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.dispatcher.ChatInputDispatcher;
import net.starly.custommenu.dispatcher.ConfirmationDispatcher;
import net.starly.custommenu.inventory.holder.MenuInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.function.Consumer;

public class SettingCenter extends InventoryListenerBase {

    private static SettingCenter instance;

    public static SettingCenter getInstance() {
        if (instance == null) instance = new SettingCenter();
        return instance;
    }

    private SettingCenter() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        
        Player player = (Player) event.getWhoClicked();
        if (inventory == player.getInventory()) return;

        Menu menu = parseMenu(event.getInventory());
        int slot = event.getSlot();

        switch (slot) {
            case 10: {
                player.closeInventory();
                MenuGlobalActionEditor.getInstance().openInventory(player, menu);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            case 11: {
                player.closeInventory();
                MenuButtonEditor.getInstance().openInventory(player, menu);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            case 13: {
                player.closeInventory();
                MenuItemEditor.getInstance().openInventory(player, menu);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            case 14: {
                Consumer<AsyncPlayerChatEvent> callback = (chatEvent) -> {
                    menu.setTitle(ChatColor.translateAlternateColorCodes('&', chatEvent.getMessage()));
                    MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "menuTitleSet")
                            .map(value -> value
                                    .replace("{menu}", menu.getId())
                                    .replace("{title}", menu.getTitle()))
                            .ifPresent(chatEvent.getPlayer()::sendMessage);

                    openInventory(player, menu);
                };
                ChatInputDispatcher.attachConsumer(player.getUniqueId(), callback);
                player.closeInventory();

                MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "enterNewTitle")
                        .ifPresent(player::sendMessage);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }

            case 15: {
                // 줄 수 변경
                ClickType clickType = event.getClick();
                int line = menu.getLine();

                if (clickType == ClickType.LEFT) {
                    if (line < 6) line++;
                } else if (clickType == ClickType.RIGHT) {
                    if (line > 1) line--;
                } else return;

                if (menu.getLine() == line) player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                else player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);

                menu.setLine(line);
                break;
            }

            case 16: {
                // 메뉴 삭제
                ConfirmationDispatcher.ConfirmationListener listener = new ConfirmationDispatcher.ConfirmationListener() {
                    private boolean selected = false;

                    @Override
                    public void onConfirm() {
                        selected = true;

                        MenuRepository menuRepository = MenuRepository.getInstance();
                        menuRepository.deleteMenu(menu.getId());

                        player.closeInventory();
                        MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "menuDeleted")
                                .map(value -> value
                                        .replace("{menu}", menu.getId()))
                                .ifPresent(player::sendMessage);

                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f, 1f);
                    }

                    @Override
                    public void onCancel() {
                        selected = true;

                        player.closeInventory();
                        openInventory(player, menu);

                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                    }

                    @Override
                    public void onClose() {
                        if (!selected) {
                            ConfirmationDispatcher.openInventory(player, "삭제 확인", this);
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        }
                    }
                };

                player.closeInventory();
                ConfirmationDispatcher.openInventory(player, "삭제 확인", listener);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                return;
            }
        }

        updateInventory(inventory, menu);
    }

    @Override
    public void openInventory(Player player, Menu menu) {
        JavaPlugin plugin = CustomMenu.getInstance();
        InventoryHolder holder = new MenuInvHolder(menu.getId());
        Inventory inventory = plugin.getServer().createInventory(holder, 27, menu.getTitle() + "§r [설정 센터]");
        updateInventory(inventory, menu);
        openInventoryAndRegisterListener(player, inventory);
    }

    private void updateInventory(Inventory inventory, Menu menu) {
        inventory.clear();

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

            for (int i = 0; i <= 9; i++) inventory.setItem(i, itemStack);
            inventory.setItem(12, itemStack);
            for (int i = 17; i <= 26; i++) inventory.setItem(i, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6글로벌액션 설정");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴에 적용되는 글로벌액션을 설정할 수 있습니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(10, itemStack);
        } {
            Material material;
            try {
                material = Material.valueOf("REDSTONE_TORCH");
            } catch (IllegalArgumentException ignored) {
                material = Material.valueOf("REDSTONE_TORCH_ON");
            }

            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6버튼 관리");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴의 버튼별 액션을 설정합니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(11, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§e아이템 설정");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴의 버튼 아이템을 설정합니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(13, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.NAME_TAG);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§e제목 설정");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴의 제목을 설정합니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(14, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.STRING);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§e줄 수 변경");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴의 줄 수를 변경합니다.", "§r§e• §7현재 : §6" + menu.getLine(), "", "§r§e• §6좌클릭 §7: §a+1", "§r§e• §6우클릭 §7: §c-1"));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(15, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c§l§n삭제");
            itemMeta.setLore(Arrays.asList("§r§e• §7메뉴를 삭제합니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(16, itemStack);
        }
    }
}

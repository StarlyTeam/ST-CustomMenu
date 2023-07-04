package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.inventory.holder.impl.MenuInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class MenuButtonEditor extends InventoryListenerBase {

    private static MenuButtonEditor instance;

    public static MenuButtonEditor getInstance() {
        if (instance == null) instance = new MenuButtonEditor();
        return instance;
    }

    private MenuButtonEditor() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;
        
        Player player = (Player) event.getWhoClicked();
        if (inventory == player.getInventory()) return;

        ItemStack currentStack = event.getCurrentItem();
        if (currentStack == null) return;

        Menu menu = parseMenu(inventory);
        ClickType clickType = event.getClick();
        int slot = event.getSlot();

        if (clickType == ClickType.LEFT) {
            unregisterListener(player.getUniqueId());
            player.closeInventory();
            ButtonActionEditor.getInstance().openInventory(player, menu, slot, 1);
        } else if (clickType == ClickType.RIGHT) {
            menu.setButton(slot, null);
            inventory.setItem(slot, null);
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = parseMenu(event.getInventory());

        JavaPlugin plugin = CustomMenu.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> SettingCenter.getInstance().openInventory(player, menu), 1L);
    }

    @Override
    public void openInventory(Player player, Menu menu) {
        JavaPlugin plugin = CustomMenu.getInstance();
        InventoryHolder holder = new MenuInvHolder(menu.getId());
        Inventory inventory = plugin.getServer().createInventory(holder, menu.getLine() * 9, menu.getTitle() + "§r [버튼 관리]");

        menu.getButtons().forEach((slot, button) -> {
            if (button == null || button.getType() == Material.AIR) return;

            ItemStack itemStack = new ItemStack(button);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemMeta.hasLore() ? itemMeta.getLore() : Arrays.asList(
                    "§r§e• §6좌클릭 §7: §a§n액션 관리",
                    "§r§e• §6우클릭 §7: §c§n삭제"
            ));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(slot, itemStack);
        });

        openInventoryAndRegisterListener(player, inventory);
    }
}

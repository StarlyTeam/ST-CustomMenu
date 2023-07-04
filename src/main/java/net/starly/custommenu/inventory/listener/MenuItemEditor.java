package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.button.MenuButton;
import net.starly.custommenu.inventory.holder.impl.MenuInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuItemEditor extends InventoryListenerBase {

    private static MenuItemEditor instance;

    public static MenuItemEditor getInstance() {
        if (instance == null) instance = new MenuItemEditor();
        return instance;
    }

    private MenuItemEditor() {}


    @Override
    protected void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        Menu menu = parseMenu(inventory);

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                menu.setButton(slot, null);
                continue;
            }

            MenuButton originButton = menu.getButton(slot);
            if (itemStack.equals(originButton)) continue;

            MenuButton newButton = new MenuButton(itemStack);
            if (originButton != null) newButton.setActionList(originButton.getActionList());
            menu.setButton(slot, newButton);
        }

        JavaPlugin plugin = CustomMenu.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> SettingCenter.getInstance().openInventory(player, menu), 1L);
    }

    @Override
    public void openInventory(Player player, Menu menu) {
        JavaPlugin plugin = CustomMenu.getInstance();
        InventoryHolder holder = new MenuInvHolder(menu.getId());
        Inventory inventory = plugin.getServer().createInventory(holder, menu.getLine() * 9, menu.getTitle() + "§r [아이템 설정]");

        menu.getButtons().forEach((slot, button) -> {
            if (button == null) return;

            ItemStack itemStack = new ItemStack(button);
            inventory.setItem(slot, itemStack);
        });

        openInventoryAndRegisterListener(player, inventory);
    }
}
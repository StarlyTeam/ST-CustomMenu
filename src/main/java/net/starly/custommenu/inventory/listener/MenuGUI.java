package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.util.CallUtil;
import net.starly.custommenu.action.expansion.listener.event.MenuButtonClickEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuCloseEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuOpenEvent;
import net.starly.custommenu.inventory.holder.impl.MenuInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.action.data.Action;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.menu.button.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class MenuGUI extends InventoryListenerBase {

    private static MenuGUI instance;

    public static MenuGUI getInstance() {
        if (instance == null) instance = new MenuGUI();
        return instance;
    }

    private MenuGUI() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        if (inventory == null) return;
        if (inventory == player.getInventory()) return;

        Menu menu = parseMenu(inventory);

        CallUtil.callMenuListeners(new MenuButtonClickEvent(event.getView(), null, event.getSlot(), event.getClick(), event.getAction(), menu));

        if (event.getCurrentItem() != null) {
            unregisterListener(player.getUniqueId());

            MenuButton button = menu.getButton(event.getSlot());
            List<Action> actionList = button.getActionList();
            for (int actionIndex = 0; actionIndex < actionList.size(); actionIndex++) {
                ActionExpansionRegistry.executeAction(actionList.get(actionIndex), menu, event.getSlot(), actionIndex, player, event.getClick());
            }

            if (inventory.getViewers().contains(player)) {
                registerListener(player.getUniqueId());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Menu menu = parseMenu(inventory);

        CallUtil.callMenuListeners(new MenuCloseEvent(event.getView(), menu));
    }

    @Override
    public void openInventory(Player player, Menu menu) {
        JavaPlugin plugin = CustomMenu.getInstance();
        InventoryHolder holder = new MenuInvHolder(menu.getId());
        Inventory inventory = plugin.getServer().createInventory(holder, menu.getLine() * 9, menu.getTitle());

        Map<Integer, MenuButton> buttons = menu.getButtons();
        for (int slot = 0; slot < menu.getLine() * 9; slot++) {
            MenuButton button = buttons.get(slot);
            if (button == null) continue;

            ItemStack itemStack = new ItemStack(button);
            inventory.setItem(slot, itemStack);
        }

        InventoryView view = openInventoryAndRegisterListener(player, inventory);
        CallUtil.callMenuListeners(new MenuOpenEvent(view, menu));
    }
}

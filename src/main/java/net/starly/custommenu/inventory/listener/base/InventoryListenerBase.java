package net.starly.custommenu.inventory.listener.base;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.global.GlobalAction;
import net.starly.custommenu.inventory.holder.MenuHolder;
import net.starly.custommenu.inventory.holder.impl.ActionInvHolder;
import net.starly.custommenu.inventory.holder.impl.ActionTypeInvHolder;
import net.starly.custommenu.inventory.page.PageHolder;
import net.starly.custommenu.inventory.page.PaginationManager;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryListenerBase {

    private static final Map<UUID, Listener> clickListenerMap = new HashMap<>();
    private static final Map<UUID, Listener> closeListenerMap = new HashMap<>();

    protected void onClose(InventoryCloseEvent event) {}
    protected void onClick(InventoryClickEvent event) {}
    public abstract void openInventory(Player player, Menu menu);


    protected InventoryView openInventoryAndRegisterListener(Player player, Inventory inventory) {
        clickListenerMap.put(player.getUniqueId(), registerClickListener(player.getUniqueId()));
        closeListenerMap.put(player.getUniqueId(), registerCloseEvent(player.getUniqueId()));
        return player.openInventory(inventory);
    }

    private Listener registerCloseEvent(UUID uniqueId) {
        Server server = CustomMenu.getInstance().getServer();

        Listener listener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryCloseEvent.class, listener, EventPriority.NORMAL, (listeners, event) -> {
            if (!(event instanceof InventoryCloseEvent)) return;
            InventoryCloseEvent closeEvent = (InventoryCloseEvent) event;

            Player player = (Player) closeEvent.getPlayer();
            if (uniqueId.equals(player.getUniqueId())) {
                unregisterListener(uniqueId);
                onClose(closeEvent);
            }
        }, CustomMenu.getInstance());

        return listener;
    }

    private Listener registerClickListener(UUID uniqueId) {
        Server server = CustomMenu.getInstance().getServer();

        Listener listener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryClickEvent.class, listener, EventPriority.NORMAL, (listeners, event) -> {
            if (!(event instanceof InventoryClickEvent)) return;
            InventoryClickEvent clickEvent = (InventoryClickEvent) event;

            Player player = (Player) clickEvent.getWhoClicked();
            if (uniqueId.equals(player.getUniqueId())) {
                onClick(clickEvent);
            }
        }, CustomMenu.getInstance());

        return listener;
    }

    protected void unregisterListener(UUID uniqueId) {
        Listener clickListener = InventoryListenerBase.clickListenerMap.remove(uniqueId);
        if (clickListener != null) {
            InventoryClickEvent.getHandlerList().unregister(clickListener);
        }

        Listener closeListener = InventoryListenerBase.closeListenerMap.remove(uniqueId);
        if (closeListener != null) {
            InventoryCloseEvent.getHandlerList().unregister(closeListener);
        }
    }


    protected Menu parseMenu(Inventory inventory) {
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        String menuId = holder.getMenuId();
        MenuRepository menuRepository = MenuRepository.getInstance();
        return menuRepository.getMenu(menuId);
    }

    protected Action parseGeneralAction(Inventory inventory) {
        Menu menu = parseMenu(inventory);
        ActionInvHolder holder = (ActionInvHolder) inventory.getHolder();
        int slot = holder.getSlot();
        int index = holder.getActionIndex();
        return menu.getButton(slot).getActionList().get(index);
    }

    protected GlobalAction parseGlobalAction(Inventory inventory) {
        Menu menu = parseMenu(inventory);
        ActionTypeInvHolder holder = (ActionTypeInvHolder) inventory.getHolder();
        return menu.getGlobalAction(holder.getActionType());
    }

    protected PaginationManager parsePaginationManager(Inventory inventory) {
        PageHolder holder = (PageHolder) inventory.getHolder();
        return holder.getPaginationManager();
    }
}

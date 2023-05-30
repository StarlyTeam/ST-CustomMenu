package net.starly.menu.inventory.listener;

import net.starly.menu.CustomMenu;
import net.starly.menu.menu.Menu;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GUIListenerBase {

    private static final Map<UUID, Listener> listeners = new HashMap<>();


    protected void onClose(InventoryCloseEvent event) {}
    protected void onClick(InventoryClickEvent event) {}
    public abstract void openInventory(Player player, Menu destination);


    protected void openInventoryAndRegisterListener(Player player, Inventory inventory) {
        player.openInventory(inventory);
        Listener listener = registerInventoryClickEvent(player.getUniqueId());
        listeners.put(player.getUniqueId(), listener);
        registerInventoryCloseEvent(player.getUniqueId());
    }

    private void registerInventoryCloseEvent(UUID uuid) {
        Server server = CustomMenu.getInstance().getServer();

        Listener closeEventListener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryCloseEvent.class, closeEventListener, EventPriority.LOWEST, (listeners, event) -> {
            if (!(event instanceof InventoryCloseEvent)) return;
            InventoryCloseEvent closeEvent = (InventoryCloseEvent) event;

            Player player = (Player) closeEvent.getPlayer();
            if (uuid.equals(player.getUniqueId())) {
                Listener listener = GUIListenerBase.listeners.remove(uuid);
                if (listener != null) {
                    InventoryClickEvent.getHandlerList().unregister(listener);
                }
                InventoryCloseEvent.getHandlerList().unregister(closeEventListener);

                onClose(closeEvent);
            }
        }, CustomMenu.getInstance());
    }

    private Listener registerInventoryClickEvent(UUID uuid) {
        Server server = CustomMenu.getInstance().getServer();
        Listener listener = new Listener() {};
        server.getPluginManager().registerEvent(InventoryClickEvent.class, listener, EventPriority.LOWEST, (listeners, event) -> {
            if (!(event instanceof InventoryClickEvent)) return;
            InventoryClickEvent clickEvent = (InventoryClickEvent) event;

            Player player = (Player) clickEvent.getWhoClicked();
            if (uuid.equals(player.getUniqueId())) {
                onClick(clickEvent);
            }
        }, CustomMenu.getInstance());

        return listener;
    }
}

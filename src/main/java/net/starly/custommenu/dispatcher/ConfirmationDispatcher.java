package net.starly.custommenu.dispatcher;

import net.starly.custommenu.CustomMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfirmationDispatcher implements Listener {

    private ConfirmationDispatcher() {}

    private static final Map<UUID, ConfirmationListener> listenerMap = new HashMap<>();

    private void handleEvent(InventoryClickEvent event) {
        ConfirmationListener listener = listenerMap.get(event.getWhoClicked().getUniqueId());
        if (listener != null) {
            event.setCancelled(true);

            int slot = event.getSlot();
            if (slot == 3) {
                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getServer().getScheduler().runTask(plugin, listener::onConfirm);
            } else if (slot == 5) {
                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getServer().getScheduler().runTask(plugin, listener::onCancel);
            } else {
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            }
        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        ConfirmationListener listener = listenerMap.remove(event.getPlayer().getUniqueId());
        if (listener != null) {
            listener.onClose();

            InventoryClickEvent.getHandlerList().unregister(this);
            InventoryCloseEvent.getHandlerList().unregister(this);
        }
    }

    public static void openInventory(Player player, String title, ConfirmationListener listener) {
        JavaPlugin plugin = CustomMenu.getInstance();
        Inventory inventory = plugin.getServer().createInventory(null, InventoryType.DISPENSER, title);

        {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("GREEN_STAINED_GLASS_PANE"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 5);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r");
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(0, itemStack);
            inventory.setItem(6, itemStack);
        } {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("RED_STAINED_GLASS_PANE"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 14);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r");
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(2, itemStack);
            inventory.setItem(8, itemStack);
        } {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("BLACK_STAINED_GLASS_PANE"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 15);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r");
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(1, itemStack);
            inventory.setItem(4, itemStack);
            inventory.setItem(7, itemStack);
        } {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("GREEN_WOOL"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("WOOL"), 1, (byte) 5);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§a§l§nO");
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(3, itemStack);
        } {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("RED_WOOL"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("WOOL"), 1, (byte) 14);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c§l§nX");
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(5, itemStack);
        }

        player.openInventory(inventory);
        plugin.getServer().getPluginManager().registerEvents(new ConfirmationDispatcher(), plugin);


        listenerMap.put(player.getUniqueId(), listener);
    }


    public interface ConfirmationListener {
        void onConfirm();
        void onCancel();
        void onClose();
    }
}

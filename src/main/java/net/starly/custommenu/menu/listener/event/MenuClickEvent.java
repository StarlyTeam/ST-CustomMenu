package net.starly.custommenu.menu.listener.event;

import lombok.Getter;
import net.starly.custommenu.menu.Menu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

public class MenuClickEvent extends InventoryClickEvent {

    @Getter private final Menu menu;

    public MenuClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, Menu menu) {
        super(view, type, slot, click, action);

        this.menu = menu;
    }
}

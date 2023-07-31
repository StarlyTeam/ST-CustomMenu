package net.starly.custommenu.action.expansion.listener.event;

import lombok.Getter;
import net.starly.custommenu.menu.Menu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

public class MenuButtonClickEvent extends InventoryClickEvent {

    @Getter private final Menu menu;

    public MenuButtonClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, Menu menu) {
        super(view, type, slot, click, action);

        this.menu = menu;
    }
}

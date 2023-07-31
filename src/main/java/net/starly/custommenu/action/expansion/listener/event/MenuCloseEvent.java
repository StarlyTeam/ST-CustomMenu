package net.starly.custommenu.action.expansion.listener.event;

import lombok.Getter;
import net.starly.custommenu.menu.Menu;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class MenuCloseEvent extends InventoryCloseEvent {

    @Getter private final Menu menu;

    public MenuCloseEvent(InventoryView transaction, Menu menu) {
        super(transaction);

        this.menu = menu;
    }
}

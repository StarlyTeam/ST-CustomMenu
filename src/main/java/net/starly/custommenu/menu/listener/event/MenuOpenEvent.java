package net.starly.custommenu.menu.listener.event;

import lombok.Getter;
import net.starly.custommenu.menu.Menu;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;

public class MenuOpenEvent extends InventoryOpenEvent {

    @Getter private final Menu menu;

    public MenuOpenEvent(InventoryView transaction, Menu menu) {
        super(transaction);

        this.menu = menu;
    }
}

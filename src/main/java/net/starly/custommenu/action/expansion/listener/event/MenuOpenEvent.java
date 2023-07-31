package net.starly.custommenu.action.expansion.listener.event;

import lombok.Getter;
import net.starly.custommenu.menu.Menu;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends InventoryOpenEvent {

    @Getter @NotNull private final Menu menu;

    public MenuOpenEvent(InventoryView transaction, @NotNull Menu menu) {
        super(transaction);

        this.menu = menu;
    }
}

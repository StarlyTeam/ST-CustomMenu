package net.starly.custommenu.action.expansion.event;

import lombok.Getter;
import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.expansion.IExecuteEvent;
import net.starly.custommenu.action.global.GlobalAction;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class GlobalActionExecuteEvent implements IExecuteEvent {

    @Getter private final Menu menu;
    @Getter private final String actionType;
    @Getter private final GlobalAction action;
    @Getter private final Player player;
    @Getter private final ClickType clickType;

    public GlobalActionExecuteEvent(@NotNull Menu menu, String actionType, @NotNull GlobalAction action, @NotNull Player player, ClickType clickType) {
        this.menu = menu;
        this.actionType = actionType;
        this.action = action;
        this.player = player;
        this.clickType = clickType;
    }
}

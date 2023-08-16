package net.starly.custommenu.expansion.action.event;

import lombok.Getter;
import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.jetbrains.annotations.NotNull;

public class ActionExecuteEvent implements ActionExecuteEventBase {

    @Getter private final Menu menu;
    @Getter private final int slot;
    @Getter private final int actionIndex;
    @Getter private final Action action;
    @Getter private final Player player;
    @Getter private final ClickType clickType;

    public ActionExecuteEvent(@NotNull Menu menu, int slot, int actionIndex, @NotNull Action action, @NotNull Player player, @NotNull ClickType clickType) {
        this.menu = menu;
        this.slot = slot;
        this.actionIndex = actionIndex;
        this.action = action;
        this.player = player;
        this.clickType = clickType;
    }
}

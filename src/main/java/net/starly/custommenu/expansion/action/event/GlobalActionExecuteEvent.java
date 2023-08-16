package net.starly.custommenu.expansion.action.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.expansion.action.data.GlobalAction;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@AllArgsConstructor
public class GlobalActionExecuteEvent implements ActionExecuteEventBase {

    @Getter private final Menu menu;
    @Getter private final String actionType;
    @Getter private final GlobalAction action;
    @Getter private final Player player;
    @Getter private final ClickType clickType;
}

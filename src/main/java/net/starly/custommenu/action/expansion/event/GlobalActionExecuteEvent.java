package net.starly.custommenu.action.expansion.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.action.data.GlobalAction;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@Getter
@AllArgsConstructor
public class GlobalActionExecuteEvent implements IActionExecuteEvent {

    private final Menu menu;
    private final String actionType;
    private final GlobalAction action;
    private final Player player;
    private final ClickType clickType;
}

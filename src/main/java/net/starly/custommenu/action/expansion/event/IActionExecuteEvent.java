package net.starly.custommenu.action.expansion.event;

import net.starly.custommenu.action.data.IAction;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface IActionExecuteEvent {

    Menu getMenu();

    IAction getAction();

    Player getPlayer();

    ClickType getClickType();
}

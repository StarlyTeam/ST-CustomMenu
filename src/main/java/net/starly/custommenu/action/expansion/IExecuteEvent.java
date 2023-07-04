package net.starly.custommenu.action.expansion;

import net.starly.custommenu.action.Action;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface IExecuteEvent {

    Menu getMenu();

    Action getAction();

    Player getPlayer();

    ClickType getClickType();
}

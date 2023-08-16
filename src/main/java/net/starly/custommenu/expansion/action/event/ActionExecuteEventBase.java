package net.starly.custommenu.expansion.action.event;

import net.starly.custommenu.expansion.action.data.ActionBase;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface ActionExecuteEventBase {

    Menu getMenu();

    ActionBase getAction();

    Player getPlayer();

    ClickType getClickType();
}

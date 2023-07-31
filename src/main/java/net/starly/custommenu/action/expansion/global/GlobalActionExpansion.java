package net.starly.custommenu.action.expansion.global;

import net.starly.custommenu.action.expansion.IActionExpansion;
import net.starly.custommenu.action.expansion.event.GlobalActionExecuteEvent;
import net.starly.custommenu.action.expansion.listener.MenuListener;
import net.starly.custommenu.action.expansion.listener.registry.MenuListenerRegistry;
import net.starly.custommenu.action.data.GlobalAction;

import java.util.List;

public abstract class GlobalActionExpansion implements IActionExpansion {

    public abstract GlobalAction parseAction(List<String> args);

    public abstract boolean onExecute(GlobalActionExecuteEvent event);

    public static boolean registerExpansion(GlobalActionExpansion expansion) {
        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        if (expansionRegistry == null) return false;
        if (expansionRegistry.isExpansionRegistered(expansion.getActionType())) return false;

        expansionRegistry.registerExpansion(expansion);
        return true;
    }

    public static boolean registerListener(MenuListener listener) {
        MenuListenerRegistry listenerRegistry = MenuListenerRegistry.getInstance();
        if (listenerRegistry == null) return false;
        if (listenerRegistry.isListenerRegistered(listener)) return false;

        listenerRegistry.registerListener(listener);
        return true;
    }
}

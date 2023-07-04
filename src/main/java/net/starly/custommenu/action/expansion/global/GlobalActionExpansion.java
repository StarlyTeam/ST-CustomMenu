package net.starly.custommenu.action.expansion.global;

import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.listener.MenuListener;
import net.starly.custommenu.action.expansion.listener.MenuListenerRegistry;
import net.starly.custommenu.action.global.GlobalAction;

import java.util.List;

public abstract class GlobalActionExpansion implements IExpansion {

    public abstract GlobalAction parseAction(List<String> args);

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

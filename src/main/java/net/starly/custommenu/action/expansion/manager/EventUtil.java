package net.starly.custommenu.action.expansion.manager;

import net.starly.custommenu.action.exception.ActionExecutionException;
import net.starly.custommenu.action.expansion.IExecuteEvent;
import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.expansion.listener.MenuListenerRegistry;
import net.starly.custommenu.event.MenuButtonClickEvent;
import net.starly.custommenu.event.MenuCloseEvent;
import net.starly.custommenu.event.MenuOpenEvent;

public class EventUtil {

    private EventUtil() {}


    public static void callExecuteEvent(IExecuteEvent event) {
        String actionType = event.getAction().getActionType();
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        GlobalActionExpansionRegistry globalExpansionRegistry = GlobalActionExpansionRegistry.getInstance();

        IExpansion expansion = expansionRegistry.isExpansionRegistered(actionType) ?
                expansionRegistry.getExpansion(actionType) :
                globalExpansionRegistry.getExpansion(actionType);
        if (expansion == null) throw new IllegalArgumentException("expansion is null");

        boolean success = expansion.onExecute(event);
        if (!success) throw new ActionExecutionException("Failed to execute action");
    }

    public static void callMenuListeners(MenuOpenEvent event) {
        MenuListenerRegistry listenerRegistry = MenuListenerRegistry.getInstance();
        listenerRegistry.getAllListener().forEach(listener -> listener.onOpen(event));
    }

    public static void callMenuListeners(MenuCloseEvent event) {
        MenuListenerRegistry listenerRegistry = MenuListenerRegistry.getInstance();
        listenerRegistry.getAllListener().forEach(listener -> listener.onClose(event));
    }

    public static void callMenuListeners(MenuButtonClickEvent event) {
        MenuListenerRegistry listenerRegistry = MenuListenerRegistry.getInstance();
        listenerRegistry.getAllListener().forEach(listener -> listener.onClick(event));
    }
}

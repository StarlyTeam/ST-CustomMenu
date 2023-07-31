package net.starly.custommenu.util;

import net.starly.custommenu.action.expansion.IActionExpansion;
import net.starly.custommenu.action.exception.ActionExecutionException;
import net.starly.custommenu.action.exception.ExpansionNotFoundException;
import net.starly.custommenu.action.expansion.event.ActionExecuteEvent;
import net.starly.custommenu.action.expansion.event.GlobalActionExecuteEvent;
import net.starly.custommenu.action.expansion.event.IActionExecuteEvent;
import net.starly.custommenu.action.expansion.general.ActionExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.expansion.listener.registry.MenuListenerRegistry;
import net.starly.custommenu.action.expansion.listener.event.MenuButtonClickEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuCloseEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuOpenEvent;

public class CallUtil {

    private CallUtil() {}


    public static void callExecuteEvent(ActionExecuteEvent event) {
        String actionType = event.getAction().getActionType();
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        ActionExpansion expansion = expansionRegistry.getExpansion(actionType);
        if (expansion == null) throw new ExpansionNotFoundException("액션 확장 플러그인을 찾을 수 없습니다. [" + actionType + "]");

        boolean success = expansion.onExecute(event);
        if (!success) throw new ActionExecutionException("액션 실행에 실패했습니다.");
    }

    public static void callExecuteEvent(GlobalActionExecuteEvent event) {
        String actionType = event.getAction().getActionType();
        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        GlobalActionExpansion expansion = expansionRegistry.getExpansion(actionType);
        if (expansion == null) throw new ExpansionNotFoundException("액션 확장 플러그인을 찾을 수 없습니다. [" + actionType + "]");

        boolean success = expansion.onExecute(event);
        if (!success) throw new ActionExecutionException("액션 실행에 실패했습니다.");
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

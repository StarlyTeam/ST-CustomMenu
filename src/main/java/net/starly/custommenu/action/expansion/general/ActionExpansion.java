package net.starly.custommenu.action.expansion.general;

import net.starly.custommenu.action.expansion.IActionExpansion;
import net.starly.custommenu.action.data.Action;
import net.starly.custommenu.action.expansion.event.ActionExecuteEvent;

import java.util.List;

public abstract class ActionExpansion implements IActionExpansion {

    public abstract Action parseAction(List<String> args);

    public abstract boolean onExecute(ActionExecuteEvent event);

    public static boolean registerExpansion(ActionExpansion expansion) {
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        if (expansionRegistry == null) return false;
        if (expansionRegistry.isExpansionRegistered(expansion.getActionType())) return false;

        expansionRegistry.registerExpansion(expansion);
        return true;
    }
}

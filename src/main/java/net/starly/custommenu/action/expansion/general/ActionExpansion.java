package net.starly.custommenu.action.expansion.general;

import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.expansion.IExpansion;

import java.util.List;

public abstract class ActionExpansion implements IExpansion {

    public abstract Action parseAction(List<String> args);

    public static boolean registerExpansion(ActionExpansion expansion) {
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        if (expansionRegistry == null) return false;
        if (expansionRegistry.isExpansionRegistered(expansion.getActionType())) return false;

        expansionRegistry.registerExpansion(expansion);
        return true;
    }
}

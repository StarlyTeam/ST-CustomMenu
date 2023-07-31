package net.starly.custommenu.action.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.action.expansion.general.ActionExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class Action implements IAction, Serializable {

    private final String actionType;

    private final List<String> args;

    public ActionExpansion getExpansion() {
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        return expansionRegistry.getExpansion(actionType);
    }
}

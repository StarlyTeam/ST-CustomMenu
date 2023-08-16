package net.starly.custommenu.expansion.action.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.expansion.action.ActionExpansion;
import net.starly.custommenu.expansion.action.registry.ActionExpansionRegistry;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
public abstract class Action implements ActionBase, Serializable {

    @Getter private final String actionType;
    @Getter private final List<String> args;

    public ActionExpansion getExpansion() {
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        return expansionRegistry.getExpansion(actionType);
    }
}

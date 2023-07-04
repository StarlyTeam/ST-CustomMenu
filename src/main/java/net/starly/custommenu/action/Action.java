package net.starly.custommenu.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.global.GlobalAction;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class Action implements Serializable {

    private String actionType;
    private List<String> args;

    public IExpansion getExpansion() {
        if (this instanceof GlobalAction) {
            GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
            return expansionRegistry.getExpansion(actionType);
        } else {
            ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
            return expansionRegistry.getExpansion(actionType);
        }
    }
}

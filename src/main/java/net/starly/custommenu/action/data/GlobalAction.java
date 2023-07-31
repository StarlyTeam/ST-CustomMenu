package net.starly.custommenu.action.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class GlobalAction implements IAction, Serializable {

    private final String actionType;

    private final List<String> args;

    @Setter private boolean enabled;

    public GlobalActionExpansion getExpansion() {
        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        return (GlobalActionExpansion) expansionRegistry.getExpansion(getActionType());
    }
}

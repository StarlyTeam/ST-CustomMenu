package net.starly.custommenu.expansion.action.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.starly.custommenu.expansion.action.GlobalActionExpansion;
import net.starly.custommenu.expansion.action.registry.GlobalActionExpansionRegistry;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
public abstract class GlobalAction implements ActionBase, Serializable {

    @Getter private final String actionType;
    @Getter private final List<String> args;
    @Getter @Setter private boolean enabled;

    public GlobalActionExpansion getExpansion() {
        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        return expansionRegistry.getExpansion(getActionType());
    }
}

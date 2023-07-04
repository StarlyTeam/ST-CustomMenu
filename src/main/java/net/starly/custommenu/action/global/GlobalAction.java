package net.starly.custommenu.action.global;

import lombok.Getter;
import lombok.Setter;
import net.starly.custommenu.action.Action;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public abstract class GlobalAction extends Action implements Serializable {

    @Getter @Setter private boolean enabled;

    public GlobalAction(@NotNull String actionType, @NotNull List<String> args, boolean enabled) {
        super(actionType, args);

        this.enabled = enabled;
    }
}

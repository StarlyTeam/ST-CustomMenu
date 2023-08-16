package net.starly.custommenu.service.action.global;

import net.starly.custommenu.expansion.action.data.GlobalAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnClickAction extends GlobalAction {

    public OnClickAction(@NotNull String actionType, @NotNull List<String> args, boolean enabled) {
        super(actionType, args, enabled);
    }
}

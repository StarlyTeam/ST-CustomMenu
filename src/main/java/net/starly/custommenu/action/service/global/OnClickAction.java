package net.starly.custommenu.action.service.global;

import net.starly.custommenu.action.global.GlobalAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnClickAction extends GlobalAction {

    public OnClickAction(@NotNull String actionType, @NotNull List<String> args, boolean enabled) {
        super(actionType, args, enabled);
    }
}

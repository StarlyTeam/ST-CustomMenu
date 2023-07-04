package net.starly.custommenu.action.service.global;

import net.starly.custommenu.action.global.GlobalAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnCloseAction extends GlobalAction {

    public OnCloseAction(@NotNull String actionType, @NotNull List<String> args, boolean enabled) {
        super(actionType, args, enabled);
    }
}

package net.starly.custommenu.service.action.general;

import net.starly.custommenu.expansion.action.data.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandAction extends Action {

    public CommandAction(@NotNull String actionType, @NotNull List<String> args) {
        super(actionType, args);
    }
}

package net.starly.custommenu.action.service;

import net.starly.custommenu.action.data.Action;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandAction extends Action {

    public CommandAction(@NotNull String actionType, @NotNull List<String> args) {
        super(actionType, args);
    }
}

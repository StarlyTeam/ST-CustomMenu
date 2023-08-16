package net.starly.custommenu.expansion.action;

import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.expansion.action.event.ActionExecuteEvent;

import java.util.List;

public abstract class ActionExpansion implements ActionExpansionBase {

    public abstract Action parseAction(List<String> args);

    public abstract boolean onExecute(ActionExecuteEvent event);
}

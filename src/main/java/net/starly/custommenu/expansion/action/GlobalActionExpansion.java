package net.starly.custommenu.expansion.action;

import net.starly.custommenu.expansion.action.event.GlobalActionExecuteEvent;
import net.starly.custommenu.expansion.action.data.GlobalAction;

import java.util.List;

public abstract class GlobalActionExpansion implements ActionExpansionBase {

    public abstract GlobalAction parseAction(List<String> args);

    public abstract boolean onExecute(GlobalActionExecuteEvent event);
}

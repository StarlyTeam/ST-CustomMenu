package net.starly.custommenu.expansion.command;

import net.starly.custommenu.expansion.ExpansionBase;

public abstract class CommandExpansion extends SubCommandExecutor implements ExpansionBase {

    public abstract String getLabelName();
}

package net.starly.custommenu.expansion.command.registry;

import com.google.common.base.Preconditions;
import net.starly.custommenu.command.CustomMenuCmdExecutor;
import net.starly.custommenu.expansion.command.CommandExpansion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExpansionRegistry {

    private static CommandExpansionRegistry instance;

    public static CommandExpansionRegistry getInstance() {
        if (instance == null) instance = new CommandExpansionRegistry();
        return instance;
    }

    private CommandExpansionRegistry() {}


    private final Map<String, CommandExpansion> expansionMap = new HashMap<>();

    public boolean registerExpansion(CommandExpansion expansion) {
        Preconditions.checkNotNull(expansion, "expansion");
        if (isExpansionRegistered(expansion.getLabelName())) return false;

        expansionMap.put(expansion.getLabelName(), expansion);
        CustomMenuCmdExecutor.getCommandExecutors().put(expansion.getLabelName(), expansion);
        return true;
    }

    public CommandExpansion getExpansion(String labelName) {
        Preconditions.checkNotNull(labelName, "labelName");

        return expansionMap.get(labelName);
    }

    public List<CommandExpansion> getAllExpansion() {
        return new ArrayList<>(expansionMap.values());
    }

    public boolean isExpansionRegistered(String labelName) {
        Preconditions.checkNotNull(labelName, "labelName");

        return expansionMap.containsKey(labelName);
    }

    public boolean unregisterExpansion(String labelName) {
        Preconditions.checkNotNull(labelName, "labelName");
        if (!isExpansionRegistered(labelName)) return false;

        expansionMap.remove(labelName);
        CustomMenuCmdExecutor.getCommandExecutors().remove(labelName);
        return true;
    }

    public void unregisterAllExpansion() {
        expansionMap.keySet().forEach(this::unregisterExpansion);
    }
}

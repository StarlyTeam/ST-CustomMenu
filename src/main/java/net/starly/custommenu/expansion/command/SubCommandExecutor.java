package net.starly.custommenu.expansion.command;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public abstract class SubCommandExecutor {

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public abstract Optional<String> getPermission();

    public boolean hasPermission(CommandSender sender) {
        return getPermission()
                .map(sender::hasPermission)
                .orElse(true);
    }
}

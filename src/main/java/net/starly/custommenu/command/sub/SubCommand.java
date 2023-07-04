package net.starly.custommenu.command.sub;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    boolean execute(CommandSender sender, String[] args);

    String getPermission();
}

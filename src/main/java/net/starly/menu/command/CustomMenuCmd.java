package net.starly.menu.command;

import net.starly.menu.message.MessageContext;
import net.starly.menu.message.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomMenuCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        MessageContext messageContext = MessageContext.getInstance();
        if (!(sender instanceof Player)) {
            messageContext.getMessage(MessageType.ERROR, "wrongPlatform").ifPresent(sender::sendMessage);
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            messageContext.getMessage(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
            return true;
        }

        switch (args[0]) {


            default: {
                messageContext.getMessage(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                return true;
            }
        }
    }
}

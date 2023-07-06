package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExpansionCommandExecutor implements SubCommand {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public ExpansionCommandExecutor() {
        subCommands.put("목록", new ExpansionListCommand());
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        } else {
            String permission = subCommand.getPermission();
            if (permission != null && !sender.hasPermission(permission)) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                        .ifPresent(sender::sendMessage);
                return false;
            }

            return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }

    @Override
    public String getPermission() {
        return null;
    }
}

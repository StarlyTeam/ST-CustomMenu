package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class ExpansionListCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length != 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        GlobalActionExpansionRegistry globalExpansionRegistry = GlobalActionExpansionRegistry.getInstance();
        List<String> expansionList = expansionRegistry.getAllExpansion().stream().map(IExpansion::getActionType).collect(Collectors.toList());
        List<String> globalExpansionList = globalExpansionRegistry.getAllExpansion().stream().map(IExpansion::getActionType).collect(Collectors.toList());

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "expansionList")
                .ifPresent(message -> {
                    message = message.replace("{expansion}", "§r§6" + String.join("§7, §6", expansionList));
                    message = message.replace("{globalExpansion}", "§r§6" + String.join("§7, §6", globalExpansionList));
                    sender.sendMessage(message);
                });
        return false;
    }

    @Override
    public String getPermission() {
        return "starly.custommenu.expansion.list";
    }
}

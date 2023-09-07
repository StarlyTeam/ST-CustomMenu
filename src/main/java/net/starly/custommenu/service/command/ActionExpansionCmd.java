package net.starly.custommenu.service.command;

import net.starly.custommenu.expansion.action.ActionExpansionBase;
import net.starly.custommenu.expansion.action.registry.ActionExpansionRegistry;
import net.starly.custommenu.expansion.action.registry.GlobalActionExpansionRegistry;
import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ActionExpansionCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length != 1) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
        GlobalActionExpansionRegistry globalExpansionRegistry = GlobalActionExpansionRegistry.getInstance();

        List<String> expansionList = expansionRegistry.getAllExpansion().stream()
                .map(ActionExpansionBase::getActionType)
                .collect(Collectors.toList());
        List<String> globalExpansionList = globalExpansionRegistry.getAllExpansion().stream()
                .map(ActionExpansionBase::getActionType)
                .collect(Collectors.toList());

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "actionExpansionList")
                .ifPresent(message -> sender.sendMessage(
                        message
                                .replace("{expansion}", "§r§6" + String.join("§7, §6", expansionList))
                                .replace("{globalExpansion}", "§r§6" + String.join("§7, §6", globalExpansionList))
                ));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of("starly.custommenu.expansion.action");
    }
}

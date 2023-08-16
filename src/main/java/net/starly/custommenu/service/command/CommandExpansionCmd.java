package net.starly.custommenu.service.command;

import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.expansion.command.CommandExpansion;
import net.starly.custommenu.expansion.command.registry.CommandExpansionRegistry;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandExpansionCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length != 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        CommandExpansionRegistry expansionRegistry = CommandExpansionRegistry.getInstance();

        List<String> expansionList = expansionRegistry.getAllExpansion().stream()
                .map(CommandExpansion::getLabelName)
                .collect(Collectors.toList());

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "commandExpansionList")
                .ifPresent(message -> sender.sendMessage(
                        message
                                .replace("{expansion}", "§r§6" + String.join("§7, §6", expansionList))
                ));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of("starly.custommenu.expansion.list");
    }
}

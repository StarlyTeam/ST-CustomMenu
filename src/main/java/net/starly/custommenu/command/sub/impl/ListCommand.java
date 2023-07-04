package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length != 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        MenuRepository menuRepository = MenuRepository.getInstance();
        List<String> menuIdList = menuRepository.getMenuIdList();

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "menuList")
                .map(value -> value
                        .replace("{list}", "§r§6" + String.join("§7, §6", menuIdList) + "§r"))
                .ifPresent(sender::sendMessage);
        return true;
    }

    @Override
    public @Nullable String getPermission() {
        return "starly.custommenu.list";
    }
}

package net.starly.custommenu.service.command;

import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MenuListCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length != 1) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        MenuRepository menuRepository = MenuRepository.getInstance();
        List<String> menuIdList = menuRepository.getMenuIdList();

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "menuList")
                .map(value -> value
                        .replace("{list}", menuIdList.isEmpty() ? "§7없음" : "§6" + String.join("§7, §6", menuIdList) + "§r"))
                .ifPresent(sender::sendMessage);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of("starly.custommenu.list");
    }
}

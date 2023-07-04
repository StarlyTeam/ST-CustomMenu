package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class CreateCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuId")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length == 1) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuLine")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length == 2) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuTitle")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length != 3) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        String menuId = args[0];
        String menuTitle = args[2];

        if (!Pattern.matches("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣1-9_]+", menuId)) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongMenuId")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        int menuLine;
        try {
            String menuLineStr = args[1];
            menuLine = Integer.parseInt(menuLineStr);

            if (menuLine < 1 || menuLine > 6) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongMenuLine")
                        .ifPresent(sender::sendMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongMenuLine")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        MenuRepository menuRepository = MenuRepository.getInstance();
        if (menuRepository.getMenu(menuId) != null) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "menuAlreadyExists")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        Menu menu = new Menu(menuId, menuLine, menuTitle);
        menuRepository.putMenu(menu);
        menuRepository.saveMenu(menu);

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "menuCreated")
                .map(value -> value
                        .replace("{menu}",menuId)
                        .replace("{line}", String.valueOf(menuLine))
                        .replace("{title}", menuTitle))
                .ifPresent(sender::sendMessage);
        return true;
    }

    @Override
    public @Nullable String getPermission() {
        return "starly.custommenu.create";
    }
}

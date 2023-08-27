package net.starly.custommenu.service.command;

import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class MenuCreateCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length == 1) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuId")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length == 2) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuLine")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length == 3) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuTitle")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        String menuId = args[1];
        String menuTitle = String.join(" ",
                Arrays.copyOfRange(args, 3, args.length)
        )
                .replace("\\s", " ");

        if (!Pattern.matches("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣1-9_]+", menuId)) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongMenuId")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        int menuLine;
        try {
            String menuLineStr = args[2];
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
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("<ID>");
        } else if (args.length == 2) {
            completions.add("<줄>");
            completions.addAll(Arrays.asList("1", "2", "3", "4", "5", "6"));
        } else {
            completions.add("<제목>");
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.of("starly.custommenu.create");
    }
}

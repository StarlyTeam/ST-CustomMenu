package net.starly.custommenu.command;

import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.command.sub.impl.*;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomMenuExecutor implements TabExecutor {

    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CustomMenuExecutor() {
        subCommands.put("생성", new CreateCommand());
        subCommands.put("삭제", new DeleteCommand());
        subCommands.put("편집", new EditCommand());
        subCommands.put("목록", new ListCommand());
        subCommands.put("열기", new OpenCommand());
        subCommands.put("애드온", new ExpansionCommandExecutor());
        subCommands.put("리로드", new ReloadCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
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
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("starly.menu.open")) completions.add("열기");
            if (sender.hasPermission("starly.menu.create")) completions.add("생성");
            if (sender.hasPermission("starly.menu.edit")) completions.add("편집");
            if (sender.hasPermission("starly.menu.delete")) completions.add("삭제");
            if (sender.hasPermission("starly.menu.list")) completions.add("목록");
            if (sender.hasPermission("starly.menu.expansion")) completions.add("애드온");
            if (sender.hasPermission("starly.menu.reload")) completions.add("리로드");
        } else if (args.length == 2) {
            if (Arrays.asList("열기", "편집", "삭제").contains(args[0])) {
                completions.add("<ID>");
                completions.addAll(MenuRepository.getInstance().getMenuIdList());
            } else if ("생성".equals(args[0])) completions.add("<ID>");
            else if ("애드온".equals(args[0])) completions.add("목록");
        } else if (args.length == 3) {
            if ("생성".equals(args[0])) completions.add("<줄>");
        } else if (args.length == 4) {
            if ("생성".equals(args[0])) completions.add("<제목>");
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}

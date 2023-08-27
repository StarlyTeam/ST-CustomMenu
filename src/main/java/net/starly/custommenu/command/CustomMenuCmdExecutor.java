package net.starly.custommenu.command;

import lombok.Getter;
import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.service.command.*;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CustomMenuCmdExecutor implements TabExecutor {

    @Getter private static final Map<String, SubCommandExecutor> commandExecutors = new HashMap<>();

    private CustomMenuCmdExecutor() {
        commandExecutors.put("생성", new MenuCreateCmd());
        commandExecutors.put("삭제", new MenuDeleteCmd());
        commandExecutors.put("편집", new MenuEditCmd());
        commandExecutors.put("명령어확장", new CommandExpansionCmd());
        commandExecutors.put("목록", new MenuListCmd());
        commandExecutors.put("액션확장", new ActionExpansionCmd());
        commandExecutors.put("열기", new MenuOpenCmd());
        commandExecutors.put("리로드", new PluginReloadCmd());
    }

    public static void register(PluginCommand command) {
        CustomMenuCmdExecutor cmdManager = new CustomMenuCmdExecutor();

        command.setExecutor(cmdManager);
        command.setTabCompleter(cmdManager);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        SubCommandExecutor commandExecutor = commandExecutors.get(args[0]);
        if (commandExecutor == null) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        Optional<String> permission = commandExecutor.getPermission();
        if (permission.isPresent() && !sender.hasPermission(permission.get())) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        return commandExecutor.onCommand(sender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            commandExecutors.forEach((subLabel, commandExecutor) -> {
                if (commandExecutor.hasPermission(sender)) {
                    completions.add(subLabel);
                }
            });

            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        } else {
            SubCommandExecutor commandExecutor = commandExecutors.get(args[0]);
            if (commandExecutor == null) return Collections.emptyList();

            return commandExecutor.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
    }
}

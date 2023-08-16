package net.starly.custommenu.service.command;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.inventory.listener.MenuGUI;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuOpenCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        Player target;
        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuId")
                    .ifPresent(sender::sendMessage);
            return false;
        } else if (args.length == 1) {
            if (!(sender instanceof Player)) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongPlatform")
                        .ifPresent(sender::sendMessage);
                return false;
            }

            if (!sender.hasPermission("starly.custommenu.open." + args[0] + ".self")) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                        .ifPresent(sender::sendMessage);
                return false;
            }

            target = (Player) sender;
        } else if (args.length == 2) {
            if (!sender.hasPermission("starly.custommenu.open." + args[0] + ".other")) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                        .ifPresent(sender::sendMessage);
                return false;
            }

            JavaPlugin plugin = CustomMenu.getInstance();
            target = plugin.getServer().getPlayerExact(args[1]);
            if (target == null) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "playerNotFound")
                        .ifPresent(sender::sendMessage);
                return false;
            }
        } else {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        String menuId = args[0];

        MenuRepository menuRepository = MenuRepository.getInstance();
        Menu menu = menuRepository.getMenu(menuId);
        if (menu == null) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "menuNotFound")
                    .ifPresent(sender::sendMessage);
            return false;
        }

        MenuGUI.getInstance().openInventory(target, menu);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("<ID>");

            MenuRepository menuRepository = MenuRepository.getInstance();
            completions.addAll(menuRepository.getMenuIdList());
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.empty();
    }
}

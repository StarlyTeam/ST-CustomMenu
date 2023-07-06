package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.inventory.listener.MenuGUI;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class OpenCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (!(sender instanceof Player)) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongPlatform")
                    .ifPresent(sender::sendMessage);
            return false;
        }
        Player player = (Player) sender;

        Player target;
        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuId")
                    .ifPresent(player::sendMessage);
            return false;
        } else if (args.length == 1) {
            if (!player.hasPermission("starly.custommenu.open." + args[0] + ".self")) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                        .ifPresent(player::sendMessage);
                return false;
            }

            target = player;
        } else if (args.length == 2) {
            if (!player.hasPermission("starly.custommenu.open." + args[0] + ".other")) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "noPermission")
                        .ifPresent(player::sendMessage);
                return false;
            }

            JavaPlugin plugin = CustomMenu.getInstance();
            target = plugin.getServer().getPlayerExact(args[1]);
            if (target == null) {
                messageContent.getMessageAfterPrefix(MessageType.ERROR, "playerNotFound")
                        .ifPresent(player::sendMessage);
                return false;
            }
        } else {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand")
                    .ifPresent(player::sendMessage);
            return false;
        }

        String menuId = args[0];

        MenuRepository menuRepository = MenuRepository.getInstance();
        Menu menu = menuRepository.getMenu(menuId);
        if (menu == null) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "menuNotFound")
                    .ifPresent(player::sendMessage);
            return false;
        }

        MenuGUI.getInstance().openInventory(target, menu);
        return true;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}

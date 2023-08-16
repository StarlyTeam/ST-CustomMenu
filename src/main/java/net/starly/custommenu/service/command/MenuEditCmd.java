package net.starly.custommenu.service.command;

import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.inventory.listener.SettingCenter;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class MenuEditCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        MessageContent messageContent = MessageContent.getInstance();

        if (!(sender instanceof Player)) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "wrongPlatform")
                    .ifPresent(sender::sendMessage);
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            messageContent.getMessageAfterPrefix(MessageType.ERROR, "noMenuId")
                    .ifPresent(player::sendMessage);
            return false;
        } else if (args.length != 1) {
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

        SettingCenter.getInstance().openInventory(player, menu);
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
        return Optional.of("starly.custommenu.edit");
    }
}

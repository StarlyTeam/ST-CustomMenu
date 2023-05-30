package net.starly.menu.command.tabcomplete;

import net.starly.menu.repo.MenuRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomMenuTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("starly.menu.open")) completions.add("열기");
            if (sender.hasPermission("starly.menu.create")) completions.add("생성");
            if (sender.hasPermission("starly.menu.edit")) completions.add("편집");
            if (sender.hasPermission("starly.menu.delete")) completions.add("삭제");
            if (sender.hasPermission("starly.menu.list")) completions.add("목록");
            if (sender.hasPermission("starly.menu.reload")) completions.add("리로드");
        } else if (args.length == 2) {
            if (Arrays.asList("열기", "편집", "삭제").contains(args[0])) {
                completions.addAll(MenuRepository.getInstance().getMenuIdList());
            } else if ("생성".equals(args[0])) {
                completions.add("<ID>");
            }
        } else if (args.length == 3) {
            if ("생성".equals(args[0])) {
                completions.add("<줄>");
            }
        } else if (args.length == 4) {
            if ("생성".equals(args[0])) {
                completions.add("<타이틀>");
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}

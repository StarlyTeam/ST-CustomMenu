package net.starly.custommenu.action.service;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.expansion.IExecuteEvent;
import net.starly.custommenu.action.expansion.general.ActionExpansion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CommandActionExpansion extends ActionExpansion {

    public void register() {
        if (!registerExpansion(this)) {
            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("액션 등록에 실패했습니다. [" + getActionType() + "]");
        }
    }


    @Override
    public String getActionType() {
        return "명령어";
    }

    @Override
    public String getKoreanName() {
        return "명령어 실행";
    }

    @Override
    public JavaPlugin getProvider() {
        return CustomMenu.getInstance();
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getDescription() {
        return "§e명령어를 실행합니다. §7(사용법 : &6명령어 <콘솔/플레이어> <명령어...>&7)";
    }

    @Override
    public String getAuthor() {
        return "Starly Team";
    }

    @Override
    public Material getItemType() {
        Material material;
        try {
            material = Material.valueOf("COMMAND_BLOCK");
        } catch (IllegalArgumentException ignored) {
            material = Material.valueOf("COMMAND");
        }

        return material;
    }

    @Override
    public Action parseAction(List<String> args) {
        return new CommandAction(getActionType(), args);
    }

    @Override
    public boolean onExecute(IExecuteEvent event) {
        JavaPlugin plugin = CustomMenu.getInstance();

        Action action = event.getAction();
        List<String> args = action.getArgs();
        if (args.size() == 0) {
            plugin.getLogger().warning("명령어 실행타입을 선언해주세요. <콘솔/플레이어>");
            return false;
        }

        String performType = args.get(0);
        String command = String.join(" ", args.subList(1, args.size()))
                .replace("{player}", event.getPlayer().getName());
        if (performType.equals("콘솔")) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
            return true;
        } else if (performType.equals("플레이어")) {
            Player player = event.getPlayer();
            player.performCommand(command);
            return true;
        } else {
            plugin.getLogger().warning("명령어 실행타입을 다시 확인해주세요. <콘솔/플레이어> :: " + performType);
            return false;
        }
    }
}

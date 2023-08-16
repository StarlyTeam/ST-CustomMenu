package net.starly.custommenu.service.action.general;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.expansion.action.event.ActionExecuteEvent;
import net.starly.custommenu.expansion.action.ActionExpansion;
import net.starly.custommenu.expansion.action.registry.ActionExpansionRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class CommandActionExpansion extends ActionExpansion {

    private CommandActionExpansion() {}

    public static void register() {
        CommandActionExpansion expansion = new CommandActionExpansion();
        ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();

        boolean success = true;
        if (expansionRegistry == null) success = false;
        if (expansionRegistry.isExpansionRegistered(expansion.getActionType())) success = false;

        expansionRegistry.registerExpansion(expansion);

        if (!success) {
            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("액션 등록에 실패했습니다. [" + expansion.getActionType() + "]");
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
        return "1.1";
    }

    @Override
    public List<String> getDescriptionLore() {
        return Arrays.asList("§6명령어를 실행합니다.", "§7{player}로 실행한 플레이어의 이름을 입력할 수 있습니다.", "§7명령어 <콘솔|플레이어|오피> <명령어>...");
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
    public boolean onExecute(ActionExecuteEvent event) {
        JavaPlugin plugin = CustomMenu.getInstance();

        Action action = event.getAction();
        List<String> args = action.getArgs();
        if (args.isEmpty()) {
            plugin.getLogger().warning("명령어 실행타입을 선언해주세요.");
            return false;
        }

        String performType = args.get(0);
        String command = String.join(" ", args.subList(1, args.size()))
                .replace("{player}", event.getPlayer().getName());

        switch (performType) {
            case "콘솔": {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
                return true;
            }

            case "플레이어": {
                Player player = event.getPlayer();
                player.performCommand(command);
                return true;
            }

            case "오피": {
                Player player = event.getPlayer();
                final boolean isOp = player.isOp();

                try {
                    player.setOp(true);
                    player.performCommand(command);
                } finally {
                    player.setOp(isOp);
                }
                return true;
            }

            default: {
                plugin.getLogger().warning("명령어 실행타입을 다시 확인해주세요. [예상된 값 : 콘솔, 플레이어, 오피] [입력된 값 : " + performType + "]");
                return false;
            }
        }
    }
}

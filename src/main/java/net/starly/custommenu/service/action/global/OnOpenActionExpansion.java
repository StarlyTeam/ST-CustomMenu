package net.starly.custommenu.service.action.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.event.GlobalActionExecuteEvent;
import net.starly.custommenu.expansion.action.GlobalActionExpansion;
import net.starly.custommenu.expansion.action.registry.GlobalActionExpansionRegistry;
import net.starly.custommenu.menu.listener.MenuListener;
import net.starly.custommenu.menu.listener.registry.MenuListenerRegistry;
import net.starly.custommenu.util.CallUtil;
import net.starly.custommenu.expansion.action.data.GlobalAction;
import net.starly.custommenu.menu.listener.event.MenuOpenEvent;
import net.starly.custommenu.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class OnOpenActionExpansion extends GlobalActionExpansion {

    private OnOpenActionExpansion() {}

    public static void register() {
        OnOpenActionExpansion expansion = new OnOpenActionExpansion();

        {
            GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
            boolean success = expansionRegistry.registerExpansion(expansion);

            if (!success) {
                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getLogger().warning("글로벌액션 등록에 실패했습니다. [글로벌@" + expansion.getActionType() + "]");
            }
        } {
            MenuListenerRegistry listenerRegistry = MenuListenerRegistry.getInstance();
            boolean success = listenerRegistry.registerListener(new Listener(expansion));

            if (!success) {
                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getLogger().warning("메뉴 리스너 등록에 실패했습니다. [글로벌@" + expansion.getActionType() + "]");
            }
        }
    }


    @Override
    public String getActionType() {
        return "입장";
    }

    @Override
    public String getKoreanName() {
        return "§e메뉴 입장";
    }

    @Override
    public JavaPlugin getProvider() {
        return CustomMenu.getInstance();
    }

    @Override
    public String getAuthor() {
        JavaPlugin plugin = CustomMenu.getInstance();
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        JavaPlugin plugin = CustomMenu.getInstance();
        return plugin.getDescription().getVersion();
    }

    @Override
    public List<String> getDescriptionLore() {
        return Arrays.asList("§6메뉴를 열었을 때 실행되는 액션입니다.");
    }

    @Override
    public Material getItemType() {
        return Material.valueOf("IRON_DOOR");
    }

    @Override
    public GlobalAction parseAction(List<String> args) {
        return new OnOpenAction(getActionType(), args, false);
    }

    @Override
    public boolean onExecute(GlobalActionExecuteEvent event) {
        return GlobalActionExpansionRegistry.executeAllAction(event);
    }


    @AllArgsConstructor
    private static class Listener extends MenuListener {

        @Getter private final OnOpenActionExpansion expansion;

        @Override
        public void onOpen(MenuOpenEvent event) {
            Menu menu = event.getMenu();
            GlobalAction action = menu.getGlobalAction(expansion.getActionType());

            if (action.isEnabled()) {
                GlobalActionExecuteEvent executeEvent = new GlobalActionExecuteEvent(menu, expansion.getActionType(), action, (Player) event.getPlayer(), null);
                CallUtil.callExecuteEvent(executeEvent);
            }
        }
    }
}

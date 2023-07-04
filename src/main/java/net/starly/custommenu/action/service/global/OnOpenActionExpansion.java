package net.starly.custommenu.action.service.global;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.expansion.IExecuteEvent;
import net.starly.custommenu.action.expansion.event.GlobalActionExecuteEvent;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.expansion.listener.MenuListener;
import net.starly.custommenu.action.expansion.manager.EventUtil;
import net.starly.custommenu.action.global.GlobalAction;
import net.starly.custommenu.event.MenuButtonClickEvent;
import net.starly.custommenu.event.MenuCloseEvent;
import net.starly.custommenu.event.MenuOpenEvent;
import net.starly.custommenu.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class OnOpenActionExpansion extends GlobalActionExpansion {

    public void register() {
        if (!registerExpansion(this)) {
            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("글로벌액션 등록에 실패했습니다. [글로벌@" + getActionType() + "]");
        }

        if (!registerListener(new Listener())) {
            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("메뉴 리스너 등록에 실패했습니다. [글로벌@" + getActionType() + "]");
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
    public String getDescription() {
        return "§f메뉴를 열었을 때 실행되는 액션입니다.";
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
    public boolean onExecute(IExecuteEvent event) {
        return GlobalActionExpansionRegistry.executeAllAction(event);
    }


    private class Listener implements MenuListener {

        @Override public void onClick(MenuButtonClickEvent event) {}
        @Override public void onClose(MenuCloseEvent event) {}

        @Override
        public void onOpen(MenuOpenEvent event) {
            Menu menu = event.getMenu();
            GlobalAction action = menu.getGlobalAction(getActionType());

            if (action.isEnabled()) {
                GlobalActionExecuteEvent executeEvent = new GlobalActionExecuteEvent(menu, getActionType(), action, (Player) event.getPlayer(), null);
                EventUtil.callExecuteEvent(executeEvent);
            }
        }
    }
}

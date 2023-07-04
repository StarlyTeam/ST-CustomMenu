package net.starly.custommenu.action.expansion.general;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.IExpansionRegistry;
import net.starly.custommenu.action.expansion.event.ActionExecuteEvent;
import net.starly.custommenu.action.expansion.manager.EventUtil;
import net.starly.custommenu.action.global.GlobalAction;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionExpansionRegistry implements IExpansionRegistry {

    private static ActionExpansionRegistry instance;

    public static ActionExpansionRegistry getInstance() {
        if (instance == null) instance = new ActionExpansionRegistry();
        return instance;
    }

    private ActionExpansionRegistry() {}


    private final Map<String, IExpansion> expansionMap = new HashMap<>();

    @Override
    public void registerExpansion(IExpansion expansion) {
        expansionMap.put(expansion.getActionType(), expansion);
    }

    @Override
    public IExpansion getExpansion(String type) {
        return expansionMap.get(type);
    }

    @Override
    public List<IExpansion> getAllExpansion() {
        return new ArrayList<>(expansionMap.values());
    }

    @Override
    public boolean isExpansionRegistered(String type) {
        return expansionMap.containsKey(type);
    }

    @Override
    public void unregisterExpansion(String type) {
        expansionMap.remove(type);
    }

    @Override
    public void unregisterAll() {
        expansionMap.clear();
    }


    public static void executeAction(Action action, Menu menu, int slot, int actionIndex, Player player, ClickType clickType) {
        if (action instanceof GlobalAction) throw new IllegalArgumentException("GlobalAction is not supported");

        try {
            ActionExecuteEvent executeEvent = new ActionExecuteEvent(menu, slot, actionIndex, action, player, clickType);
            EventUtil.callExecuteEvent(executeEvent);
        } catch (Exception ex) {
            ex.printStackTrace();

            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("액션 실행에 실패했습니다. [" + menu.getId() + "@" + slot + ":" + actionIndex + "]");
        }
    }
}

package net.starly.custommenu.expansion.action.registry;

import net.starly.core.util.PreCondition;
import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.expansion.action.ActionExpansion;
import net.starly.custommenu.expansion.action.event.ActionExecuteEvent;
import net.starly.custommenu.util.CallUtil;
import net.starly.custommenu.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionExpansionRegistry {

    private static ActionExpansionRegistry instance;

    public static ActionExpansionRegistry getInstance() {
        if (instance == null) instance = new ActionExpansionRegistry();
        return instance;
    }

    private ActionExpansionRegistry() {}


    private final Map<String, ActionExpansion> expansionMap = new HashMap<>();

    public boolean registerExpansion(ActionExpansion expansion) {
        PreCondition.nonNull(expansion, "expansion");
        if (isExpansionRegistered(expansion.getActionType())) return false;

        expansionMap.put(expansion.getActionType(), expansion);
        return true;
    }

    public ActionExpansion getExpansion(String actionType) {
        PreCondition.nonNull(actionType, "actionType");

        return expansionMap.get(actionType);
    }

    public List<ActionExpansion> getAllExpansion() {
        return new ArrayList<>(expansionMap.values());
    }

    public boolean isExpansionRegistered(String actionType) {
        PreCondition.nonNull(actionType, "actionType");

        return expansionMap.containsKey(actionType);
    }

    public boolean unregisterExpansion(String actionType) {
        PreCondition.nonNull(actionType, "actionType");
        if (!isExpansionRegistered(actionType)) return false;

        expansionMap.remove(actionType);
        return true;
    }

    public void unregisterAllExpansion() {
        expansionMap.keySet().forEach(this::unregisterExpansion);
    }


    public static void executeAction(Action action, Menu menu, int slot, int actionIndex, Player player, ClickType clickType) {
        try {
            ActionExecuteEvent executeEvent = new ActionExecuteEvent(menu, slot, actionIndex, action, player, clickType);
            CallUtil.callExecuteEvent(executeEvent);
        } catch (Exception ex) {
            ex.printStackTrace();

            JavaPlugin plugin = CustomMenu.getInstance();
            plugin.getLogger().warning("액션 실행에 실패했습니다. [" + menu.getId() + "@" + slot + ":" + actionIndex + "]");
        }
    }
}

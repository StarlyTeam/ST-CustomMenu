package net.starly.custommenu.action.expansion.general;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.data.Action;
import net.starly.custommenu.action.expansion.event.ActionExecuteEvent;
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

    public void registerExpansion(ActionExpansion expansion) {
        expansionMap.put(expansion.getActionType(), expansion);
    }

    public ActionExpansion getExpansion(String type) {
        return expansionMap.get(type);
    }

    public List<ActionExpansion> getAllExpansion() {
        return new ArrayList<>(expansionMap.values());
    }

    public boolean isExpansionRegistered(String type) {
        return expansionMap.containsKey(type);
    }

    public void unregisterExpansion(String type) {
        expansionMap.remove(type);
    }

    public void unregisterAll() {
        expansionMap.clear();
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

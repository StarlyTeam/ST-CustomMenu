package net.starly.custommenu.expansion.action.registry;

import com.google.common.base.Preconditions;
import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.expansion.action.data.GlobalAction;
import net.starly.custommenu.expansion.action.GlobalActionExpansion;
import net.starly.custommenu.expansion.action.event.GlobalActionExecuteEvent;
import net.starly.custommenu.expansion.action.ActionExpansion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlobalActionExpansionRegistry {

    private static GlobalActionExpansionRegistry instance;

    public static GlobalActionExpansionRegistry getInstance() {
        if (instance == null) instance = new GlobalActionExpansionRegistry();
        return instance;
    }

    private GlobalActionExpansionRegistry() {}


    private final Map<String, GlobalActionExpansion> expansionMap = new HashMap<>();

    public boolean registerExpansion(GlobalActionExpansion expansion) {
        Preconditions.checkNotNull(expansion, "expansion");
        if (isExpansionRegistered(expansion.getActionType())) return false;

        expansionMap.put(expansion.getActionType(), expansion);
        return true;
    }

    public GlobalActionExpansion getExpansion(String actionType) {
        Preconditions.checkNotNull(actionType, "actionType");

        return expansionMap.get(actionType);
    }

    public List<GlobalActionExpansion> getAllExpansion() {
        return new ArrayList<>(expansionMap.values());
    }

    public boolean isExpansionRegistered(String actionType) {
        Preconditions.checkNotNull(actionType, "actionType");

        return expansionMap.containsKey(actionType);
    }

    public boolean unregisterExpansion(String actionType) {
        Preconditions.checkNotNull(actionType, "actionType");
        if (!isExpansionRegistered(actionType)) return false;

        expansionMap.remove(actionType);
        return true;
    }

    public void unregisterAllExpansion() {
        expansionMap.keySet().forEach(this::unregisterExpansion);
    }


    public static boolean executeAllAction(GlobalActionExecuteEvent event) {
        GlobalAction action = event.getAction();
        List<String> args = action.getArgs();

        boolean success = true;
        for (int actionIndex = 0; actionIndex < args.size(); actionIndex++) {
            try {
                String arg = args.get(actionIndex);
                List<String> parsedArgs = Arrays.asList(arg.split(" "));

                ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
                ActionExpansion expansion = expansionRegistry.getExpansion(parsedArgs.get(0));
                if (expansion == null) throw new IllegalArgumentException("액션 확장 플러그인을 찾을 수 없습니다. [글로벌@" + action.getActionType() + "]");

                Action parsedAction = expansion.parseAction(parsedArgs.subList(1, parsedArgs.size()));
                ActionExpansionRegistry.executeAction(parsedAction, event.getMenu(), -1, actionIndex, event.getPlayer(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
                success = false;

                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getLogger().warning("액션 실행에 실패했습니다. [" + event.getMenu().getId() + "@글로벌:" + action.getActionType() + "]");
            }
        }

        return success;
    }
}

package net.starly.custommenu.action.expansion.global;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.Action;
import net.starly.custommenu.action.expansion.IExecuteEvent;
import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.IExpansionRegistry;
import net.starly.custommenu.action.expansion.general.ActionExpansion;
import net.starly.custommenu.action.expansion.general.ActionExpansionRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlobalActionExpansionRegistry implements IExpansionRegistry {

    private static GlobalActionExpansionRegistry instance;

    public static GlobalActionExpansionRegistry getInstance() {
        if (instance == null) instance = new GlobalActionExpansionRegistry();
        return instance;
    }

    private GlobalActionExpansionRegistry() {}


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


    public static boolean executeAllAction(IExecuteEvent event) {
        Action action = event.getAction();
        List<String> args = action.getArgs();

        boolean success = true;
        for (int actionIndex = 0; actionIndex < args.size(); actionIndex++) {
            try {
                String arg = args.get(actionIndex);
                List<String> parsedArgs = Arrays.asList(arg.split(" "));

                ActionExpansionRegistry expansionRegistry = ActionExpansionRegistry.getInstance();
                ActionExpansion expansion = (ActionExpansion) expansionRegistry.getExpansion(parsedArgs.get(0));
                if (expansion == null) throw new IllegalArgumentException("액션 확장자를 찾을 수 없습니다. [글로벌@" + action.getActionType() + "]");

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

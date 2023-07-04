package net.starly.custommenu.menu;

import lombok.Getter;
import lombok.Setter;
import net.starly.custommenu.action.expansion.IExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.global.GlobalAction;
import net.starly.custommenu.button.MenuButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu implements Serializable {

    @Setter private Map<Integer, MenuButton> buttonMap = new HashMap<>();
    @Setter private Map<String, GlobalAction> globalActionMap = new HashMap<>();

    @Getter @Setter private String id;
    @Getter @Setter private int line;
    @Getter @Setter private String title;

    public Menu(String id, int line, String title) {
        this.id = id;
        this.line = line;
        this.title = title;
    }


    public MenuButton getButton(int slot) {
        return buttonMap.get(slot);
    }

    public Map<Integer, MenuButton> getButtons() {
        return new HashMap<>(buttonMap);
    }

    public void setButton(int slot, MenuButton button) {
        if (button == null) buttonMap.remove(slot);
        else buttonMap.put(slot, button);
    }


    public GlobalAction getGlobalAction(String actionType) {
        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        GlobalActionExpansion expansion = (GlobalActionExpansion) expansionRegistry.getExpansion(actionType);
        if (!globalActionMap.containsKey(actionType)) {
            if (expansion == null) return null;
            globalActionMap.put(actionType, expansion.parseAction(new ArrayList<>()));
        }

        return globalActionMap.get(actionType);
    }

    public List<GlobalAction> getGlobalActions() {
        return new ArrayList<>(globalActionMap.values());
    }

    public void putGlobalAction(GlobalAction action) {
        globalActionMap.put(action.getActionType(), action);
    }
}

package net.starly.menu.menu;

import lombok.Getter;
import lombok.Setter;
import net.starly.menu.menu.button.MenuButton;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Menu implements Serializable {

    private final Map<Integer, MenuButton> buttonMap = new HashMap<>();

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
        buttonMap.put(slot, button);
    }

    public void setButtons(Map<Integer, MenuButton> buttons) {
        buttonMap.clear();
        buttonMap.putAll(buttons);
    }
}

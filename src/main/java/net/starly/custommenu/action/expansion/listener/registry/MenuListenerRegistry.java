package net.starly.custommenu.action.expansion.listener.registry;

import net.starly.custommenu.action.expansion.listener.MenuListener;

import java.util.ArrayList;
import java.util.List;

public class MenuListenerRegistry {

    private static MenuListenerRegistry instance;

    public static MenuListenerRegistry getInstance() {
        if (instance == null) instance = new MenuListenerRegistry();
        return instance;
    }

    private MenuListenerRegistry() {}


    private final List<MenuListener> listeners = new ArrayList<>();

    public void registerListener(MenuListener listener) {
        listeners.add(listener);
    }

    public List<MenuListener> getAllListener() {
        return new ArrayList<>(listeners);
    }

    public boolean isListenerRegistered(MenuListener listener) {
        return listeners.contains(listener);
    }

    public void unregisterListener(MenuListener listener) {
        listeners.remove(listener);
    }

    public void unregisterAll() {
        listeners.clear();
    }
}

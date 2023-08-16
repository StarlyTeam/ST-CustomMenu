package net.starly.custommenu.menu.listener.registry;

import net.starly.custommenu.menu.listener.MenuListener;

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

    public boolean registerListener(MenuListener listener) {
        if (listener == null) return false;
        if (isListenerRegistered(listener)) return false;

        listeners.add(listener);
        return true;
    }

    public List<MenuListener> getAllListener() {
        return new ArrayList<>(listeners);
    }

    public boolean isListenerRegistered(MenuListener listener) {
        if (listener == null) return false;

        return listeners.contains(listener);
    }

    public boolean unregisterListener(MenuListener listener) {
        if (listener == null) return false;
        if (!isListenerRegistered(listener)) return false;

        listeners.remove(listener);
        return true;
    }

    public void unregisterAllListener() {
        listeners.clear();
    }
}

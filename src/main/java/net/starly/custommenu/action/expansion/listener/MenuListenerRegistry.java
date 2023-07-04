package net.starly.custommenu.action.expansion.listener;

import java.util.ArrayList;
import java.util.List;

public class MenuListenerRegistry {

    private static MenuListenerRegistry instance;

    public static MenuListenerRegistry getInstance() {
        if (instance == null) instance = new MenuListenerRegistry();
        return instance;
    }

    private MenuListenerRegistry() {}


    private final List<MenuListener> listenerList = new ArrayList<>();

    public void registerListener(MenuListener listener) {
        listenerList.add(listener);
    }

    public List<MenuListener> getAllListener() {
        return new ArrayList<>(listenerList);
    }

    public boolean isListenerRegistered(MenuListener listener) {
        return listenerList.contains(listener);
    }

    public void unregisterListener(MenuListener listener) {
        listenerList.remove(listener);
    }

    public void unregisterAll() {
        listenerList.clear();
    }
}

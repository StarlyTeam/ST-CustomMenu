package net.starly.menu.repo;

import net.starly.menu.menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuRepository {

    private static MenuRepository instance;

    public static MenuRepository getInstance() {
        if (instance == null) instance = new MenuRepository();
        return instance;
    }

    private MenuRepository() {}


    private final Map<String, Menu> menuMap = new HashMap<>();

    public Menu getMenu(String id) {
        return menuMap.get(id);
    }

    public List<Menu> getMenus() {
        return new ArrayList<>(menuMap.values());
    }

    public List<String> getMenuIdList() {
        return menuMap.values().stream().map(Menu::getId).collect(Collectors.toList());
    }

    public void putMenu(Menu menu) {
        menuMap.put(menu.getId(), menu);
    }

    public void removeMenu(String id) {
        menuMap.remove(id);
    }
}

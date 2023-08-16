package net.starly.custommenu.menu.repo;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.action.data.Action;
import net.starly.custommenu.expansion.action.data.GlobalAction;
import net.starly.custommenu.menu.button.MenuButton;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.util.EncodeUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
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

    public void deleteMenu(String id) {
        menuMap.remove(id);

        JavaPlugin plugin = CustomMenu.getInstance();
        File file = new File(plugin.getDataFolder(), "menu/" + id + ".yml");
        file.delete();
    }

    public void clear() {
        menuMap.clear();
    }


    public void saveAllMenu() {
        menuMap.values().forEach(this::saveMenu);
    }

    public void loadAllMenu() {
        clear();

        JavaPlugin plugin = CustomMenu.getInstance();
        File menuFolder = new File(plugin.getDataFolder(), "menu/");
        List<File> menuDataFiles = Arrays.asList(menuFolder.listFiles());
        menuDataFiles.forEach(this::loadMenu);
    }

    public void saveMenu(Menu menu) {
        try {
            JavaPlugin plugin = CustomMenu.getInstance();
            File menuFolder = new File(plugin.getDataFolder(), "menu/");
            if (!menuFolder.exists()) menuFolder.mkdirs();
            File dataFile = new File(menuFolder, menu.getId() + ".yml");
            if (!dataFile.exists()) dataFile.createNewFile();
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);


            ConfigurationSection dataSection = dataConfig.createSection("menu");
            dataSection.set("id", menu.getId());
            dataSection.set("title", menu.getTitle());
            dataSection.set("line", menu.getLine());

            ConfigurationSection buttonSection = dataSection.createSection("button");
            menu.getButtons().forEach((slot, button) -> {
                ItemStack itemStack = new ItemStack(button);
                List<Action> actionList = button.getActionList();

                buttonSection.set(slot + ".itemStack", EncodeUtil.encode(itemStack));
                buttonSection.set(slot + ".action", actionList.stream()
                        .map(EncodeUtil::encode)
                        .collect(Collectors.toList())
                );
            });

            ConfigurationSection globalActionSection = dataSection.createSection("globalAction");
            menu.getGlobalActions().forEach(action -> {
                String data = EncodeUtil.encode(action);
                globalActionSection.set(action.getActionType(), data);
            });


            dataConfig.save(dataFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadMenu(File dataFile) {
        FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        ConfigurationSection dataSection = dataConfig.getConfigurationSection("menu");
        String id = dataSection.getString("id");
        int line = dataSection.getInt("line");
        String title = dataSection.getString("title");

        ConfigurationSection buttonSection = dataSection.getConfigurationSection("button");
        Map<Integer, MenuButton> buttonDataMap = new HashMap<>();
        buttonSection.getKeys(false).forEach(slot -> {
            ItemStack itemStack = EncodeUtil.decode(buttonSection.getString(slot + ".itemStack"), ItemStack.class);
            List<Action> actionList = buttonSection.getStringList(slot + ".action").stream()
                    .map(actionData -> EncodeUtil.decode(actionData, Action.class))
                    .collect(Collectors.toList());

            MenuButton button = new MenuButton(itemStack);
            button.setActionList(actionList);
            buttonDataMap.put(Integer.parseInt(slot), button);
        });

        ConfigurationSection globalActionSection = dataSection.getConfigurationSection("globalAction");
        Map<String, GlobalAction> globalActionDataMap = new HashMap<>();
        globalActionSection.getKeys(false).forEach(actionType -> {
            String data = globalActionSection.getString(actionType);
            globalActionDataMap.put(actionType, EncodeUtil.decode(data, GlobalAction.class));
        });


        Menu menu = new Menu(id, line, title);
        menu.setButtonMap(buttonDataMap);
        menu.setGlobalActionMap(globalActionDataMap);

        putMenu(menu);
    }
}

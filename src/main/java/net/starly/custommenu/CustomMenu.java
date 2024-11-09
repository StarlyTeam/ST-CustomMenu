package net.starly.custommenu;

import lombok.Getter;
import net.starly.custommenu.configuration.GlobalPropertyManager;
import net.starly.custommenu.service.action.general.CommandActionExpansion;
import net.starly.custommenu.service.action.global.OnClickActionExpansion;
import net.starly.custommenu.service.action.global.OnCloseActionExpansion;
import net.starly.custommenu.service.action.global.OnOpenActionExpansion;
import net.starly.custommenu.command.CustomMenuCmdExecutor;
import net.starly.custommenu.dispatcher.ChatInputDispatcher;
import net.starly.custommenu.message.MessageLoader;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CustomMenu extends JavaPlugin {

    @Getter private static CustomMenu instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        File configFile = new File(getDataFolder(), "config.properties");
        if (!configFile.exists()) saveResource("config.properties", false);
        GlobalPropertyManager.getInstance().loadAll(configFile);

        File messageConfigFile = new File(getDataFolder(), "message.yml");
        if (!messageConfigFile.exists()) saveResource("message.yml", false);
        MessageLoader.load(YamlConfiguration.loadConfiguration(messageConfigFile));

        File menuFolder = new File(getDataFolder(), "menu/");
        if (!menuFolder.exists()) menuFolder.mkdirs();

        MenuRepository.getInstance().loadAllMenu();

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        CustomMenuCmdExecutor.register(getCommand("custom-menu"));

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        ChatInputDispatcher.register(this);

        /* DEFAULT EXPANSION (GENERAL)
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        CommandActionExpansion.register();

        /* DEFAULT EXPANSION (GLOBAL)
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        OnOpenActionExpansion.register();
        OnCloseActionExpansion.register();
        OnClickActionExpansion.register();
    }

    @Override
    public void onDisable() {
        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        File configFile = new File(getDataFolder(), "config.properties");
        GlobalPropertyManager.getInstance().saveAll(configFile);

        MenuRepository.getInstance().saveAllMenu();
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}

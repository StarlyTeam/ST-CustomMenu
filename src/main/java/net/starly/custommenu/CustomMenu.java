package net.starly.custommenu;

import lombok.Getter;
import net.starly.core.bstats.Metrics;
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
        /* DEPENDENCY
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        new Metrics(this, 18953);

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

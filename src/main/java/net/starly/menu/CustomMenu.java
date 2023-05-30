package net.starly.menu;

import lombok.Getter;
import net.starly.menu.command.CustomMenuCmd;
import net.starly.menu.command.tabcomplete.CustomMenuTab;
import net.starly.menu.message.MessageLoader;
import org.bukkit.command.PluginCommand;
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
//        new Metrics(this, 12345); // TODO: 수정

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        File messageConfigFile = new File(getDataFolder(), "message.yml");
        if (!messageConfigFile.exists()) saveResource("message.yml", false);
        MessageLoader.load(YamlConfiguration.loadConfiguration(messageConfigFile));

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        PluginCommand customMenuCommand = getServer().getPluginCommand("custom-menu");
        customMenuCommand.setExecutor(new CustomMenuCmd());
        customMenuCommand.setTabCompleter(new CustomMenuTab());


        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        // TODO: 수정
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}

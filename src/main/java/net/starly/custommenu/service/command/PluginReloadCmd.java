package net.starly.custommenu.service.command;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.expansion.command.SubCommandExecutor;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageLoader;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.menu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PluginReloadCmd extends SubCommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        JavaPlugin plugin = CustomMenu.getInstance();
        MessageContent messageContent = MessageContent.getInstance();

        File configFile = new File(plugin.getDataFolder(), "message.yml");
        MessageLoader.load(YamlConfiguration.loadConfiguration(configFile));
        MenuRepository.getInstance().loadAllMenu();

        messageContent.getMessageAfterPrefix(MessageType.NORMAL, "reloadComplete")
                .ifPresent(sender::sendMessage);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull Optional<String> getPermission() {
        return Optional.empty();
    }
}

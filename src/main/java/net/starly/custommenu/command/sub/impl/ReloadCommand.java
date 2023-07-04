package net.starly.custommenu.command.sub.impl;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.command.sub.SubCommand;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageLoader;
import net.starly.custommenu.message.MessageType;
import net.starly.custommenu.repo.MenuRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ReloadCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
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
    public @Nullable String getPermission() {
        return null;
    }
}

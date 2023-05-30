package net.starly.menu.message;

import net.starly.core.util.PreCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.Objects;

public class MessageLoader {

    public static void load(FileConfiguration config) {
        MessageContext.getInstance().clear();
        Arrays.stream(MessageType.values()).forEach(type -> loadMessageSection(Objects.requireNonNull(config.getConfigurationSection(type.getPath())), type));
    }

    private static void loadMessageSection(ConfigurationSection section, MessageType type) {
        PreCondition.nonNull(section, "메세지를 로드할 수 없습니다. : " + type.name());

        MessageContext msgContext = MessageContext.getInstance();
        section.getKeys(true).forEach(key -> {
            msgContext.setMessage(type, key, section.isList(key) ? "{prefix}" + String.join("\n&r{prefix}", section.getStringList(key)) : section.getString(key).replace("\\n", "\n"));
        });
    }
}
package net.starly.custommenu.message;

import com.google.common.base.Preconditions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class MessageLoader {

    public static void load(FileConfiguration config) {
        MessageContent.getInstance().clear();
        Arrays.stream(MessageType.values()).forEach(type -> loadMessageSection(config.getConfigurationSection(type.getPath()), type));
    }

    private static void loadMessageSection(ConfigurationSection section, MessageType type) {
        Preconditions.checkNotNull(type, "type");
        Preconditions.checkNotNull(section, "메세지를 로드할 수 없습니다. : " + type.name());

        MessageContent msgContext = MessageContent.getInstance();
        section.getKeys(true).forEach(key -> {
            msgContext.setMessage(type, key, section.isList(key) ? "{prefix}" + String.join("\n&r{prefix}", section.getStringList(key)) : section.getString(key).replace("\\n", "\n"));
        });
    }
}
package net.starly.custommenu.message;

import org.bukkit.ChatColor;

import java.util.*;

public class MessageContent {

    private static MessageContent instance;
    public static MessageContent getInstance() {
        if (instance == null) instance = new MessageContent();
        return instance;
    }
    private MessageContent() {}


    private final Map<MessageType, Map<String, String>> map = new HashMap<>();

    public Optional<String> getMessage(MessageType type, String key) {
        return Optional.ofNullable(map.getOrDefault(type, Collections.emptyMap()).get(key));
    }

    public Optional<String> getMessageAfterPrefix(MessageType type, String key) {
        String prefix = getPrefix().orElse("");
        return getMessage(type, key).map(message -> prefix + message);
    }

    public List<String> getMessages(MessageType type, String key) {
        List<String> messages = Arrays.asList(getMessage(type, key).orElse("").split("\n"));
        messages.replaceAll(value -> value.replace("{prefix}", ""));
        return messages;
    }

    public List<String> getMessagesAfterPrefix(MessageType type, String key) {
        List<String> messages = Arrays.asList(getMessage(type, key).orElse("").split("\n"));
        messages.replaceAll(value -> value.replace("{prefix}", getPrefix().orElse("")));
        return messages;
    }

    public void setMessage(MessageType type, String key, String value) {
        Map<String, String> typeMap = map.getOrDefault(type, new HashMap<>());
        typeMap.put(key, ChatColor.translateAlternateColorCodes('&', value));
        map.put(type, typeMap);
    }

    public void clear() {
        map.clear();
    }


    public Optional<String> getPrefix() {
        return Optional.ofNullable(map.getOrDefault(MessageType.NORMAL, Collections.emptyMap()).get("prefix"));
    }
}

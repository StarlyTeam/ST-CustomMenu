package net.starly.menu.message;

import org.bukkit.ChatColor;

import java.util.*;

public class MessageContext {

    private static MessageContext instance;
    public static MessageContext getInstance() {
        if (instance == null) instance = new MessageContext();
        return instance;
    }
    private MessageContext() {}


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


    private Optional<String> getPrefix() {
        return Optional.ofNullable(map.getOrDefault(MessageType.NORMAL, Collections.emptyMap()).get("prefix"));
    }
}

package net.starly.custommenu.dispatcher;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.message.MessageContent;
import net.starly.custommenu.message.MessageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputDispatcher implements Listener {

    private static final Map<UUID, Consumer<AsyncPlayerChatEvent>> consumers = new HashMap<>();

    public static void attachConsumer(UUID uniqueId, Consumer<AsyncPlayerChatEvent> callback) {
        consumers.put(uniqueId, callback);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        handleEvent(event);
    }

    private void handleEvent(AsyncPlayerChatEvent event) {
        Consumer<AsyncPlayerChatEvent> callback = consumers.remove(event.getPlayer().getUniqueId());
        if (callback != null) {
            event.setCancelled(true);

            if (event.getMessage().equals("-")) {
                MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "inputCancelled")
                        .ifPresent(event.getPlayer()::sendMessage);
            } else {
                JavaPlugin plugin = CustomMenu.getInstance();
                plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(event));
            }
        }
    }
}

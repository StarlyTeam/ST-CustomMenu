package net.starly.custommenu.expansion;

import org.bukkit.plugin.java.JavaPlugin;

public interface ExpansionBase {

    JavaPlugin getProvider();

    String getVersion();

    String getAuthor();
}

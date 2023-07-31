package net.starly.custommenu.action.expansion;

import net.starly.custommenu.action.data.IAction;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface IActionExpansion {

    String getActionType();

    String getKoreanName();

    JavaPlugin getProvider();

    String getVersion();

    List<String> getDescriptionLore();

    String getAuthor();

    Material getItemType();

    IAction parseAction(List<String> args);
}

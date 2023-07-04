package net.starly.custommenu.action.expansion;

import net.starly.custommenu.action.Action;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface IExpansion {

    String getActionType();

    String getKoreanName();

    JavaPlugin getProvider();

    String getVersion();

    String getDescription();

    String getAuthor();

    Material getItemType();

    Action parseAction(List<String> args);

    boolean onExecute(IExecuteEvent event);
}

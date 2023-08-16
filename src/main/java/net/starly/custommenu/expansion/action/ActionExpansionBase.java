package net.starly.custommenu.expansion.action;

import net.starly.custommenu.expansion.action.data.ActionBase;
import net.starly.custommenu.expansion.ExpansionBase;
import org.bukkit.Material;

import java.util.List;

public interface ActionExpansionBase extends ExpansionBase {

    String getActionType();

    String getKoreanName();

    List<String> getDescriptionLore();

    Material getItemType();

    ActionBase parseAction(List<String> args);
}

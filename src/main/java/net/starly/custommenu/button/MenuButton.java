package net.starly.custommenu.button;

import lombok.Getter;
import lombok.Setter;
import net.starly.custommenu.action.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuButton extends ItemStack implements Serializable {

    @Getter @Setter private List<Action> actionList;

    public MenuButton(ItemStack itemStack) {
        super(itemStack);

        this.actionList = new ArrayList<>();
    }
}

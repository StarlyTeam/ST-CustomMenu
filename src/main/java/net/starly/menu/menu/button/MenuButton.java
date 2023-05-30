package net.starly.menu.menu.button;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class MenuButton extends ItemStack implements Serializable {

    @NotNull private ItemStack itemStack;
    @Nullable private String command;
    @Nullable private String menu;
}


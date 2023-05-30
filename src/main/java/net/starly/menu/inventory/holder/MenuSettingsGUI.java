package net.starly.menu.inventory.holder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.menu.menu.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class MenuSettingsGUI implements InventoryHolder {

    @Getter private final Menu menu;

    @Override
    public @Nullable Inventory getInventory() {
        return null;
    }
}

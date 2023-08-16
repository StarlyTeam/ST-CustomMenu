package net.starly.custommenu.inventory.holder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.starly.custommenu.inventory.holder.type.MenuHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class MenuInvHolder implements InventoryHolder, MenuHolder {

    @Getter private final String menuId;

    @Override
    public @Nullable Inventory getInventory() {
        return null;
    }
}

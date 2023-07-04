package net.starly.custommenu.inventory.holder.impl;

import lombok.Getter;
import net.starly.custommenu.inventory.holder.ActionTypeHolder;
import net.starly.custommenu.inventory.holder.MenuHolder;
import net.starly.custommenu.inventory.page.PageHolder;
import net.starly.custommenu.inventory.page.PaginationManager;
import org.bukkit.inventory.InventoryHolder;

public class ActionTypeInvHolder extends PageHolder implements InventoryHolder, MenuHolder, ActionTypeHolder {

    @Getter private final String menuId;
    @Getter private final String actionType;

    public ActionTypeInvHolder(PaginationManager paginationManager, int prevBtnSlot, int nextSlotBtn, String menuId, String actionType) {
        super(paginationManager, prevBtnSlot, nextSlotBtn);
        this.menuId = menuId;
        this.actionType = actionType;
    }
}

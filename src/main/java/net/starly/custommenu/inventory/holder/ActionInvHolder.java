package net.starly.custommenu.inventory.holder;

import lombok.Getter;
import net.starly.custommenu.inventory.holder.type.ActionTypeHolder;
import net.starly.custommenu.inventory.holder.type.MenuHolder;
import net.starly.custommenu.inventory.holder.type.SlotHolder;
import net.starly.custommenu.inventory.page.PageHolder;
import net.starly.custommenu.inventory.page.PaginationManager;
import net.starly.custommenu.menu.Menu;
import org.bukkit.inventory.InventoryHolder;

public class ActionInvHolder extends PageHolder implements InventoryHolder, MenuHolder, ActionTypeHolder, SlotHolder {

    @Getter private final String menuId;
    @Getter private final String actionType;
    @Getter private final int slot;
    @Getter private final int actionIndex;

    public ActionInvHolder(PaginationManager paginationManager, int prevPage, int nextPageSlot, Menu menu, int slot, int actionIndex) {
        super(paginationManager, prevPage, nextPageSlot);
        this.menuId = menu.getId();
        this.actionType = menu.getButton(slot).getActionList().get(actionIndex).getActionType();
        this.slot = slot;
        this.actionIndex = actionIndex;
    }
}

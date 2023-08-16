package net.starly.custommenu.inventory.holder;

import lombok.Getter;
import net.starly.custommenu.inventory.holder.type.MenuHolder;
import net.starly.custommenu.inventory.holder.type.SlotHolder;
import net.starly.custommenu.inventory.page.PageHolder;
import net.starly.custommenu.inventory.page.PaginationManager;

public class ButtonInvHolder extends PageHolder implements MenuHolder, SlotHolder {

    @Getter private final String menuId;
    @Getter private final int slot;

    public ButtonInvHolder(PaginationManager paginationManager, int prevPage, int nextPageSlot, String menuId, int slot) {
        super(paginationManager, prevPage, nextPageSlot);
        this.menuId = menuId;
        this.slot = slot;
    }
}

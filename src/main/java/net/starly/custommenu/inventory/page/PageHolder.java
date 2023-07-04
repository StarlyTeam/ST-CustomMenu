package net.starly.custommenu.inventory.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@AllArgsConstructor
public class PageHolder implements InventoryHolder {

    private final PaginationManager paginationManager;

    private final int prevBtnSlot;
    private final int nextBtnSlot;


    @Override
    @Deprecated
    public Inventory getInventory() {
        throw new UnsupportedOperationException("Not supported");
    }

    public Inventory getInventory(Inventory baseInventory) {
        SinglePage currentPage = paginationManager.getCurrentPageData();

        for (int i = 0; i < currentPage.getItems().size(); i++) {
            baseInventory.setItem(i, currentPage.getItems().get(i));
        }

        {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6이전 페이지");
            itemStack.setItemMeta(itemMeta);

            baseInventory.setItem(prevBtnSlot, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6다음 페이지");
            itemStack.setItemMeta(itemMeta);

            baseInventory.setItem(nextBtnSlot, itemStack);
        }

        return baseInventory;
    }
}

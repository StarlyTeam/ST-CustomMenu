package net.starly.custommenu.inventory.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageHolder implements InventoryHolder {

    private final PaginationManager paginationManager;

    private final int prevBtnSlot;
    private final int nextBtnSlot;


    @Override
    @Deprecated
    public Inventory getInventory() {
        throw new UnsupportedOperationException("지원되지 않는 메소드 호출 입니다.");
    }

    public Inventory getInventory(Inventory baseInventory) {
        SinglePage currentPage = paginationManager.getCurrentPageData();
        List<ItemStack> items = currentPage.getItems();

        for (int i = 0; i < items.size(); i++) {
            baseInventory.setItem(i, items.get(i));
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

package net.starly.custommenu.inventory.page;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaginationManager {

    private final List<SinglePage> pages;
    private int currentPage;

    public PaginationManager(List<ItemStack> items, int itemPerPage) {
        this.pages = paginateItems(items, itemPerPage);
        this.currentPage = 1;
    }

    public void nextPage() {
        if (currentPage < pages.size()) currentPage++;
    }

    public void prevPage() {
        if (currentPage > 1) currentPage--;
    }

    public SinglePage getCurrentPageData() {
        return pages.get(currentPage - 1);
    }

    public List<SinglePage> paginateItems(List<ItemStack> items, int itemPerPage) {
        List<SinglePage> pages = new ArrayList<>();

        int itemCount = items.size();
        if (itemCount == 0) {
            pages.add(new SinglePage(0, new ArrayList<>()));
        } else {
            int pageCount = (int) Math.ceil((double) itemCount / itemPerPage);
            for (int i = 0; i < pageCount; i++) {
                int start = i * itemPerPage;
                int end = Math.min(start + itemPerPage, itemCount);
                List<ItemStack> pageItems = items.subList(start, end);
                pages.add(new SinglePage(i + 1, pageItems));
            }
        }

        return pages;
    }
}
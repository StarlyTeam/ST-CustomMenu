package net.starly.custommenu.inventory.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class SinglePage {
    @Getter private final int pageNum;
    @Getter private final List<ItemStack> items;
}

package net.starly.custommenu.inventory.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SinglePage {
    private final int pageNum;
    private final List<ItemStack> items;
}

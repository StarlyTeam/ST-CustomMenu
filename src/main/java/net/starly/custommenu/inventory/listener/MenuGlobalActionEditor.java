package net.starly.custommenu.inventory.listener;

import net.starly.custommenu.CustomMenu;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansion;
import net.starly.custommenu.action.expansion.global.GlobalActionExpansionRegistry;
import net.starly.custommenu.action.data.GlobalAction;
import net.starly.custommenu.inventory.holder.impl.MenuInvHolder;
import net.starly.custommenu.inventory.listener.base.InventoryListenerBase;
import net.starly.custommenu.menu.Menu;
import net.starly.custommenu.util.NBTUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGlobalActionEditor extends InventoryListenerBase {

    private static MenuGlobalActionEditor instance;

    public static MenuGlobalActionEditor getInstance() {
        if (instance == null) instance = new MenuGlobalActionEditor();
        return instance;
    }

    private MenuGlobalActionEditor() {}


    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);


        Inventory inventory =event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        if (inventory == player.getInventory()) return;

        if (event.getSlot() == 26) {
            player.closeInventory();
            try {
                player.playSound(player.getLocation(), Sound.valueOf("ITEM_BOOK_PAGE_TURN"), 1f, 1f);
            } catch (IllegalArgumentException ignored) {}
            return;
        } else if (event.getSlot() >= 18 && event.getSlot() <= 25) return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        String actionType = NBTUtil.getNBT(itemStack, "CUSTOMMENU_ACTIONTYPE");
        if (actionType == null || actionType.isEmpty()) return;

        Menu menu = parseMenu(inventory);
        GlobalAction action = menu.getGlobalAction(actionType);

        ClickType clickType = event.getClick();
        if (clickType == ClickType.LEFT) {
            unregisterListener(player.getUniqueId());
            player.closeInventory();
            GlobalActionDetailEditor.getInstance().openInventory(player, menu, actionType, 1);
        } else if (clickType == ClickType.RIGHT) {
            action.setEnabled(!action.isEnabled());
            updateInventory(inventory, menu);
        } else return;

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = parseMenu(event.getInventory());

        JavaPlugin plugin = CustomMenu.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> SettingCenter.getInstance().openInventory(player, menu), 1L);
    }

    @Override
    public void openInventory(Player player, Menu menu) {
        JavaPlugin plugin = CustomMenu.getInstance();
        InventoryHolder holder = new MenuInvHolder(menu.getId());
        Inventory inventory = plugin.getServer().createInventory(holder, 27, menu.getTitle() + "§r [글로벌액션]");
        updateInventory(inventory, menu);
        openInventoryAndRegisterListener(player, inventory);
    }

    private void updateInventory(Inventory inventory, Menu menu) {
        inventory.clear();

        {
            ItemStack itemStack;
            try {
                itemStack = new ItemStack(Material.valueOf("BLACK_STAINED_GLASS_PANE"));
            } catch (IllegalArgumentException ignored) {
                itemStack = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (byte) 15);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r");
            itemStack.setItemMeta(itemMeta);

            for (int i = 18; i <= 25; i++) inventory.setItem(i, itemStack);
        } {
            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§c§l§n뒤로가기");
            itemMeta.setLore(Arrays.asList("§r§e• §7뒤로 돌아갑니다."));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(26, itemStack);
        }

        GlobalActionExpansionRegistry expansionRegistry = GlobalActionExpansionRegistry.getInstance();
        List<GlobalActionExpansion> expansionList = expansionRegistry.getAllExpansion();
        for (int index = 0; index < expansionList.size(); index++) {
            GlobalActionExpansion expansion = expansionList.get(index);


            ItemStack itemStack = new ItemStack(expansion.getItemType());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§r" + expansion.getKoreanName());
            List<String> lore = expansion.getDescriptionLore().stream()
                    .map(line -> "§r§e• §f" + line).collect(Collectors.toList());
            lore.addAll(Arrays.asList(
                    "",
                    "§r§e• §7활성화 여부 : " + (menu.getGlobalAction(expansion.getActionType()).isEnabled() ? "§a§n여" : "§c§n부"),
                    "§r§e• §6좌클릭 §7: §a§n세부 설정",
                    "§r§e• §6우클릭 §7: §a§n활성 상태 변경"
            ));
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            itemStack = NBTUtil.setNBT(itemStack, "CUSTOMMENU_ACTIONTYPE", expansion.getActionType());
            inventory.setItem(index, itemStack);
        }
    }
}

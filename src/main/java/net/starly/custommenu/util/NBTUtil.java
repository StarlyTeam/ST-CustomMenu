package net.starly.custommenu.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {

    private NBTUtil() {}


    public static ItemStack setNBT(ItemStack itemStack, String key, String value) {
        NBT.modify(itemStack, (nbt) -> {
            nbt.setString(key, value);
        });

        return itemStack;
    }

    public static String getNBT(ItemStack itemStack, String key) {
        return NBT.get(itemStack, (nbt) -> {
            return nbt.getString(key);
        });
    }

    public static ItemStack removeNBT(ItemStack itemStack, String key) {
        NBT.modify(itemStack, (nbt) -> {
            nbt.removeKey(key);
        });

        return itemStack;
    }
}

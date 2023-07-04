package net.starly.custommenu.util;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {

    private NBTUtil() {}


    public static ItemStack setNBT(ItemStack itemStack, String key, String value) {
        ItemStackWrapper nmsStack = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper tagCompound = nmsStack.getTag();
        if (tagCompound == null) tagCompound = NmsItemStackUtil.getInstance().getNbtCompoundUtil().newInstance();
        tagCompound.setString(key, value);
        return NmsItemStackUtil.getInstance().asBukkitCopy(nmsStack);
    }

    public static String getNBT(ItemStack itemStack, String key) {
        ItemStackWrapper nmsStack = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper tagCompound = nmsStack.getTag();
        if (tagCompound == null) return null;
        return tagCompound.getString(key);
    }

    public static ItemStack removeNBT(ItemStack itemStack, String key) {
        ItemStackWrapper nmsStack = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper tagCompound = nmsStack.getTag();
        if (tagCompound == null) return itemStack;
        tagCompound.setString(key, null);
        return NmsItemStackUtil.getInstance().asBukkitCopy(nmsStack);
    }
}

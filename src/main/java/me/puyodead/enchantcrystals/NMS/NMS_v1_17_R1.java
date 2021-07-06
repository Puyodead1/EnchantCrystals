package me.puyodead.enchantcrystals.NMS;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NMS_v1_17_R1 implements NMSBase {

    @Override
    public void onEnchantmentPerformed(Player player, int cost, InventoryView view) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        Class<?> CraftInventoryView = ReflectionUtil.getOBCClass("inventory.CraftInventoryView");
        Class<?> CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer");
        Class<?> ItemStack = Class.forName("net.minecraft.world.item.ItemStack");

        // get the entity player
        Object craftPlayer = CraftPlayer.cast(player);
        Object entityPlayer = ReflectionUtil.getHandle(craftPlayer);

        // get container as EnchantMenu
        Object container = CraftInventoryView.cast(view);
        Object enchantmentMenu = ReflectionUtil.getHandle(container);

        // change the enchantment seed
        ReflectionUtil.invokeMethod(entityPlayer, "enchantDone", new Class[]{ItemStack, int.class}, new Object[]{null, cost});

        Object newEnchantmentSeed = ReflectionUtil.getField(entityPlayer, "cl");

        // change enchantment seed on enchant menu data slot
        Field enchantmentSeedField = enchantmentMenu.getClass().getDeclaredField("q"); // data slot/container property
        enchantmentSeedField.setAccessible(true);
        Object dataSlot = enchantmentSeedField.get(enchantmentMenu);

        ReflectionUtil.invokeMethod(dataSlot, "set", new Class[]{int.class}, new Object[]{newEnchantmentSeed});
    }
}

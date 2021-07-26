package me.puyodead.enchantcrystals.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NMS_v1_13_R2 implements NMSBase {

    @Override
    public void onEnchantmentPerformed(Player player, int cost, InventoryView view) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        Class<?> CraftInventoryView = ReflectionUtil.getOBCClass("inventory.CraftInventoryView");
        Class<?> CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer");
        Class<?> ItemStack = ReflectionUtil.getNMSClass("ItemStack");

        // get the entity player
        Object craftPlayer = CraftPlayer.cast(player);
        Object entityPlayer = ReflectionUtil.getHandle(craftPlayer);

        // get container as EnchantMenu
        Object container = CraftInventoryView.cast(view);
        Object containerEnchantTable = ReflectionUtil.getHandle(container);

        // change the enchantment seed
        ReflectionUtil.invokeMethod(entityPlayer, "enchantDone", new Class[]{ItemStack, int.class}, new Object[]{null, cost});

        Object newEnchantmentSeed = ReflectionUtil.getField(entityPlayer, "bZ");

        // change enchantment seed on ContainerEnchantTable class
        Field enchantmentSeedField = containerEnchantTable.getClass().getDeclaredField("f"); // seed property
        enchantmentSeedField.setAccessible(true);
        enchantmentSeedField.set(containerEnchantTable, newEnchantmentSeed);
    }
}

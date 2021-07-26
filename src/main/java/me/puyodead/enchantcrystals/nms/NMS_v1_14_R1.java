package me.puyodead.enchantcrystals.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NMS_v1_14_R1 implements NMSBase {

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
        Object enchantmentMenu = ReflectionUtil.getHandle(container);

        // change the enchantment seed
        ReflectionUtil.invokeMethod(entityPlayer, "enchantDone", new Class[]{ItemStack, int.class}, new Object[]{null, cost});

        Object newEnchantmentSeed = ReflectionUtil.getField(entityPlayer, "bR");

        // change enchantment seed on enchant menu container property
        Field enchantmentSeedField = enchantmentMenu.getClass().getDeclaredField("i"); // enchantSlots container property
        enchantmentSeedField.setAccessible(true);
        Object dataSlot = enchantmentSeedField.get(enchantmentMenu);

        ReflectionUtil.invokeMethod(dataSlot, "set", new Class[]{int.class}, new Object[]{newEnchantmentSeed});
    }
}

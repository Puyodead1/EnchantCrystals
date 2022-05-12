package me.puyodead1.enchantcrystals.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NMS_v1_18 implements NMSBase {

    @Override
    public void onEnchantmentPerformed(Player player, int cost, InventoryView view) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        Class<?> CraftInventoryView = ReflectionUtil.getOBCClass("inventory.CraftInventoryView");
        Class<?> CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer");
        Class<?> ItemStack = Class.forName("net.minecraft.world.item.ItemStack");

        // get the entity player
        Object craftPlayer = CraftPlayer.cast(player);
        System.out.println(craftPlayer);
        Object entityPlayer = ReflectionUtil.getHandle(craftPlayer);
        System.out.println(entityPlayer);

        // get container as EnchantMenu
        Object container = CraftInventoryView.cast(view);
        System.out.println(container);
        Object enchantmentMenu = ReflectionUtil.getHandle(container);
        System.out.println(enchantmentMenu);

        // change the enchantment seed
        // onEnchantmentPerformed on Player in net.minecraft.world.entity.player
        ReflectionUtil.invokeMethod(entityPlayer, "a", new Class[]{ItemStack, int.class}, new Object[]{null, cost});

        // enchantmentSeed on EntityPlayer in net.minecraft.world.entity.player
        Object newEnchantmentSeed = ReflectionUtil.getField(entityPlayer, "cl");
        System.out.println(newEnchantmentSeed);

        // change enchantment seed on enchant menu data slot
        // enchantmentSeed on net.minecraft.world.inventory.ContainerEnchantTable
        Field enchantmentSeedField = enchantmentMenu.getClass().getDeclaredField("q");
        System.out.println(enchantmentSeedField);
        enchantmentSeedField.setAccessible(true);
        // DataSlot aka ContainerProperty
        Object dataSlot = enchantmentSeedField.get(enchantmentMenu);
        System.out.println(dataSlot);

        // Calls the set method on net.minecraft.world.inventory.ContainerProperty
        ReflectionUtil.invokeMethod(dataSlot, "a", new Class[]{int.class}, new Object[]{newEnchantmentSeed});
    }
}

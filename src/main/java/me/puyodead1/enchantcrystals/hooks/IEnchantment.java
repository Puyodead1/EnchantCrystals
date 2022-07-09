package me.puyodead1.enchantcrystals.hooks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IEnchantment {
    ItemStack apply(final ItemStack itemStack, final int level);
    ItemStack applyUnsafe(final ItemStack itemStack, final int level);
    boolean canEnchantItem(final ItemStack itemStack);
}

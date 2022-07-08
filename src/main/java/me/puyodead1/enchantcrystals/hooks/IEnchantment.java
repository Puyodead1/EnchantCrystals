package me.puyodead1.enchantcrystals.hooks;

import org.bukkit.inventory.ItemStack;

public interface IEnchantment {
    public ItemStack apply(final ItemStack itemStack, final int level);
}

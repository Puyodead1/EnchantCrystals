package me.puyodead1.enchantcrystals.hooks.pluginhooks.mojang;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import org.bukkit.inventory.ItemStack;

public class MojangEnchantment extends Enchantment {

    public final org.bukkit.enchantments.Enchantment enchantment;

    public MojangEnchantment(org.bukkit.enchantments.Enchantment enchantment) {
        super(enchantment.getKey().toString(), enchantment.getKey().getKey(), enchantment.getMaxLevel());
        this.enchantment = enchantment;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, int level) {
        itemStack.addEnchantment(this.enchantment, level);
        return itemStack;
    }

    @Override
    public ItemStack applyUnsafe(ItemStack itemStack, int level) {
        itemStack.addUnsafeEnchantment(this.enchantment, level);
        return itemStack;
    }

    @Override
    public boolean canEnchantItem(final ItemStack itemStack) {
        return this.enchantment.canEnchantItem(itemStack);
    }
}

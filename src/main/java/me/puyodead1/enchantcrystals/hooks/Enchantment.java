package me.puyodead1.enchantcrystals.hooks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class Enchantment implements IEnchantment {
    private final String key;
    private final String name;
    private final int maxLevel;

    public Enchantment(String name, int maxLevel) {
        this.key = name.toUpperCase();
        this.name = name;
        this.maxLevel = maxLevel;
    }

    public Enchantment(String key, String name, int maxLevel) {
        this.key = key;
        this.name = name;
        this.maxLevel = maxLevel;
    }

    @Override
    public abstract ItemStack apply(final ItemStack itemStack, final int level);

    @Override
    public abstract ItemStack applyUnsafe(final ItemStack itemStack, final int level);

    @Override
    public abstract boolean canEnchantItem(final ItemStack itemStack);

    public String getKey() {
        return key;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}

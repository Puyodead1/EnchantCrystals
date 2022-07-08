package me.puyodead1.enchantcrystals.hooks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class Enchantment implements IEnchantment {
    private final String key;
    private final String name;
    private final int maxLevel;
    private final Set<Material> appliesTo;

    public Enchantment(String name, int maxLevel, Set<Material> appliesTo) {
        this.key = name.toUpperCase();
        this.name = name;
        this.maxLevel = maxLevel;
        this.appliesTo = appliesTo;
    }

    @Override
    public abstract ItemStack apply(final ItemStack itemStack, final int level);

    public String getKey() {
        return key;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Set<Material> getAppliesTo() {
        return appliesTo;
    }
}

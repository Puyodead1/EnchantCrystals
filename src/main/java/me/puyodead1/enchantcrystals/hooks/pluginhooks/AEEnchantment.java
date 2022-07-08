package me.puyodead1.enchantcrystals.hooks.pluginhooks;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class AEEnchantment extends Enchantment {
    public AEEnchantment(String name, int maxLevel, Set<Material> appliesTo) {
        super(name, maxLevel, appliesTo);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, int level) {
        return AEAPI.applyEnchant(this.getName(), level, itemStack);
    }
}

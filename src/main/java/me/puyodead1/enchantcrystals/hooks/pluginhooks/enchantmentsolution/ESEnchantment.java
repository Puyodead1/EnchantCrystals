package me.puyodead1.enchantcrystals.hooks.pluginhooks.enchantmentsolution;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.ctp.crashapi.item.ItemData;
import org.ctp.enchantmentsolution.enchantments.CustomEnchantment;
import org.ctp.enchantmentsolution.utils.items.EnchantmentUtils;

public class ESEnchantment extends Enchantment {
    private final CustomEnchantment enchantment;

    public ESEnchantment(final CustomEnchantment ce) {
        super(String.format("es:%s", ce.getName()), ce.getDisplayName(), ce.getMaxLevel());
        this.enchantment = ce;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, int level) {
        return EnchantmentUtils.addEnchantmentToItem(itemStack, enchantment, level);
    }

    @Override
    public ItemStack applyUnsafe(ItemStack itemStack, int level) {
        return apply(itemStack, level);
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return enchantment.canEnchantItem(new ItemData(itemStack));
    }
}

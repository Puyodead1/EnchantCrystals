package me.puyodead1.enchantcrystals.hooks.pluginhooks;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class AEEnchantment extends Enchantment {
    public final AdvancedEnchantment enchantment;
    public AEEnchantment(AdvancedEnchantment enchantment) {
        super(String.format("ac:%s", enchantment.getName().toLowerCase().replace(" ", "_")), enchantment.getName(), enchantment.getHighestLevel());
        this.enchantment = enchantment;
    }

    @Override
    public ItemStack apply(ItemStack itemStack, int level) {
        return AEAPI.applyEnchant(this.getName(), level, itemStack);
    }

    @Override
    public ItemStack applyUnsafe(ItemStack itemStack, int level) {
        return apply(itemStack, level);
    }

    @Override
    public boolean canEnchantItem(final ItemStack itemStack) {
        return this.enchantment.canBeApplied(itemStack.getType());
    }
}

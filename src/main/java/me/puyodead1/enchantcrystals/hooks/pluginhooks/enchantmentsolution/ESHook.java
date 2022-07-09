package me.puyodead1.enchantcrystals.hooks.pluginhooks.enchantmentsolution;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import me.puyodead1.enchantcrystals.hooks.PluginHook;
import org.ctp.enchantmentsolution.enchantments.CustomEnchantment;
import org.ctp.enchantmentsolution.enchantments.RegisterEnchantments;

public class ESHook extends PluginHook {

    public ESHook() {
        super("EnchantSolution");
    }

    @Override
    public void loadEnchantments() {
        for(final CustomEnchantment ce : RegisterEnchantments.getEnchantments()) {
            getEnchantments().add(new ESEnchantment(ce));
        }
    }

    @Override
    public Enchantment getEnchantment(String nameOrKey) {
        return null;
    }
}

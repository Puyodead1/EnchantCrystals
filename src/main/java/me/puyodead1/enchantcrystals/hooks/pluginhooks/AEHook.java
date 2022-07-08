package me.puyodead1.enchantcrystals.hooks.pluginhooks;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import me.puyodead1.enchantcrystals.hooks.IPluginHook;
import me.puyodead1.enchantcrystals.hooks.PluginHook;
import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;

public class AEHook extends PluginHook implements IPluginHook {
    public AEHook() {
        super("AdvancedEnchantments");
    }

    @Override
    public void loadEnchantments() {
        for (String e : AEAPI.getAllEnchantments()) {
            AdvancedEnchantment enchantment = AEAPI.getEnchantmentInstance(e);
            this.getEnchantments().add(new AEEnchantment(enchantment.getName(), enchantment.getHighestLevel(), enchantment.getMaterials()));
        }
    }
}

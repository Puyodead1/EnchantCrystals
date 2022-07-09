package me.puyodead1.enchantcrystals.hooks.pluginhooks.advancedenchantments;

import me.puyodead1.enchantcrystals.hooks.PluginHook;
import net.advancedplugins.ae.api.AEAPI;
import net.advancedplugins.ae.enchanthandler.enchantments.AdvancedEnchantment;

import java.util.Objects;

public class AEHook extends PluginHook {
    public AEHook() {
        super("AdvancedEnchantments");
    }

    @Override
    public void loadEnchantments() {
        for (String e : AEAPI.getAllEnchantments()) {
            getEnchantments().add(new AEEnchantment(AEAPI.getEnchantmentInstance(e)));
        }
    }

    /**
     * Gets an enchantment by its key
     * @param name enchantment name
     * @return Enchantment
     */
    @Override
    public me.puyodead1.enchantcrystals.hooks.Enchantment getEnchantment(String name) {
        return this.getEnchantments().stream().filter(p -> Objects.equals(p.getKey(), name)).findFirst().get();
    }
}

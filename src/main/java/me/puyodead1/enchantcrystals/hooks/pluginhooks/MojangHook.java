package me.puyodead1.enchantcrystals.hooks.pluginhooks;

import me.puyodead1.enchantcrystals.hooks.PluginHook;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

import java.util.Objects;

public class MojangHook extends PluginHook {

    public MojangHook() {
        super("Mojang");
    }

    @Override
    public void loadEnchantments() {
        for(final Enchantment enchantment : EnchantmentWrapper.values()) {
            this.getEnchantments().add(new MojangEnchantment(enchantment));
        }
    }


    /**
     * Gets an enchantment by its key
     * @param key enchantment key
     * @return Enchantment
     */
    @Override
    public me.puyodead1.enchantcrystals.hooks.Enchantment getEnchantment(String key) {
        return this.getEnchantments().stream().filter(p -> Objects.equals(p.getKey(), key)).findFirst().get();
    }
}

package me.puyodead1.enchantcrystals.hooks.pluginhooks.mojang;

import me.puyodead1.enchantcrystals.hooks.PluginHook;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.Objects;

public class MojangHook extends PluginHook {

    public MojangHook() {
        super("Mojang");
    }

    @Override
    public void loadEnchantments() {
        Arrays.stream(Enchantment.values()).filter(p -> p.getKey().getNamespace().equals(NamespacedKey.MINECRAFT)).forEach(p -> addEnchantment(new MojangEnchantment(p)));
    }


    /**
     * Gets an enchantment by its key
     *
     * @param key enchantment key
     * @return Enchantment
     */
    @Override
    public me.puyodead1.enchantcrystals.hooks.Enchantment getEnchantment(String key) {
        return this.getEnchantments().stream().filter(p -> Objects.equals(p.getKey(), key)).findFirst().get();
    }
}

package me.puyodead1.enchantcrystals.hooks;

public interface IPluginHook {
    void loadEnchantments();

    Enchantment getEnchantment(final String nameOrKey);
}

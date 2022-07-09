package me.puyodead1.enchantcrystals.hooks.pluginhooks;

public enum EPluginHooks {

    MOJANG("Mojang"),
    ADVANCED_ENCHANTMENTS("AdvancedEnchantments"),
    ENCHANTMENT_SOLUTION("EnchantmentSolution");
    private final String name;

    EPluginHooks(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

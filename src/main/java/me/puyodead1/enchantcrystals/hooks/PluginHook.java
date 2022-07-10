package me.puyodead1.enchantcrystals.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PluginHook implements IPluginHook {
    private final String name;
    private final List<Enchantment> enchantments;

    public PluginHook(final String name) {
        this.name = name;
        this.enchantments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(final Enchantment e) {
        this.enchantments.add(e);
    }

    /**
     * Checks if an enchantment is registered in this hook.
     *
     * @param key enchantment key
     * @return true if the enchant is registered in this hook
     */
    public boolean hasEnchantment(String key) {
        return this.enchantments.stream().anyMatch(e -> Objects.equals(e.getKey(), key));
    }

    @Override
    public abstract Enchantment getEnchantment(final String nameOrKey);
}

package me.puyodead1.enchantcrystals.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PluginHook {
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

    /**
     * Checks if an enchantment is registered in this hook.
     * @param enchantmentName Enchant display name or key to get
     * @return true if the enchant is registered in this hook
     */
    public boolean hasEnchantment(String enchantmentName) {
        return this.enchantments.stream().anyMatch(e -> Objects.equals(e.getKey(), enchantmentName) || Objects.equals(e.getName(), enchantmentName));
    }

    /**
     * Gets an enchantment
     * @param enchantmentName Enchant display name or key to get
     * @return Enchantment
     */
    public Enchantment getEnchantmentByName(String enchantmentName) {
        return this.getEnchantments().stream().filter(e -> Objects.equals(e.getKey(), enchantmentName) || Objects.equals(e.getName(), enchantmentName)).findFirst().orElseThrow(NoSuchElementException::new);
    }
}

package me.puyodead.enchantcrystals;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;

public class CrystalType {
    private final NamespacedKey key;
    private final Enchantment enchantment;
    private final int maxLevel;
    private final List<String> lore;

    private static final HashMap<NamespacedKey, CrystalType> crystals = new HashMap<>();

    public CrystalType(Enchantment enchantment, List<String> lore) {
        this.key = enchantment.getKey();
        this.enchantment = enchantment;
        this.maxLevel = enchantment.getMaxLevel();
        this.lore = lore;

        crystals.put(enchantment.getKey(), this);
    }

    public NamespacedKey getKey() {
        return key;
    }

    public String getName() {
        return enchantment.getName();
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static HashMap<NamespacedKey, CrystalType> getCrystals() {
        return crystals;
    }

    public static CrystalType valueOf(NamespacedKey key) {
        return getCrystals().getOrDefault(key, null);
    }
}

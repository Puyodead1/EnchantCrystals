package me.puyodead.enchantcrystals;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;

public class CrystalType {
    private String crystalName;
    private Enchantment crystalEnchantment;
    private int crystalMaxLevel;
    private List<String> crystalLore;

    private static HashMap<String, CrystalType> crystals = new HashMap<>();

    public CrystalType(Enchantment crystalEnchantment, List<String> crystalLore) {
        this.crystalName = crystalEnchantment.getName();
        this.crystalEnchantment = crystalEnchantment;
        this.crystalMaxLevel = crystalEnchantment.getMaxLevel();
        this.crystalLore = crystalLore;

        crystals.put(crystalEnchantment.getName(), this);
    }

    public String getCrystalName() {
        return crystalName;
    }

    public Enchantment getCrystalEnchantment() {
        return crystalEnchantment;
    }

    public List<String> getCrystalLore() {
        return crystalLore;
    }

    public int getCrystalMaxLevel() {
        return crystalMaxLevel;
    }
    public static HashMap<String, CrystalType> getCrystals() {
        return crystals;
    }

    public static CrystalType valueOf(String string) {
        return getCrystals().getOrDefault(string, null);
    }
}

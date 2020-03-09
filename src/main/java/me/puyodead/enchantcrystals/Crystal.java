package me.puyodead.enchantcrystals;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Crystal {

    private CrystalType crystalType;
    private int crystalLevel;
    private ItemStack crystalItem;

    public Crystal(CrystalType crystalType, int crystalLevel) {
        this.crystalType = crystalType;
        this.crystalLevel = crystalLevel;

        ItemStack itemStack = new ItemStack(Material.NETHER_STAR); // TODO: use UMATERIAL and config for settings material
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(EnchantCrystalsUtils.Color(EnchantCrystals.plugin.getConfig().getString("settings.crystal display name")));
        itemMeta.setLore(EnchantCrystalsUtils.ColorList(crystalType.getCrystalLore(), crystalType.getCrystalEnchantment(), crystalLevel));
        itemStack.setItemMeta(itemMeta);

        this.crystalItem = itemStack;
    }

    public CrystalType getCrystalType() {
        return crystalType;
    }

    public int getCrystalLevel() {
        return crystalLevel;
    }

    public ItemStack getCrystalItem() {
        return crystalItem;
    }
}

package me.puyodead.enchantcrystals;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Crystal {

    private final CrystalType type;
    private final int level;
    private final String name;
    private final int amount;
    private ItemStack itemStack;

    public Crystal(CrystalType type, int level) {
        this.type = type;
        this.level = level;
        this.name = type.getName();
        this.amount = 1;

        setup();
    }

    public Crystal(CrystalType type, int level, int amount) {
        this.type = type;
        this.level = level;
        this.name = type.getName();
        this.amount = amount;

        setup();
    }

    private void setup() {
        ItemStack itemStack = new ItemStack(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")), this.getType(), this.getAmount(), this.getLevel())));
        itemMeta.setLore(EnchantCrystalsUtils.colorList(this.type.getLore(), this.type, this.level));
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(this.amount);

        NBTItem nbti = new NBTItem(itemStack);
        nbti.setString("enchantmentkey", this.type.getEnchantment().getKey().toString());
        nbti.setInteger("enchantmentlevel", this.level);
        nbti.setBoolean("puyodead1:enchantcrystals", true);
        nbti.setBoolean("puyodead1:isCombinedCrystal", false);

        this.itemStack = nbti.getItem();
    }

    public CrystalType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}

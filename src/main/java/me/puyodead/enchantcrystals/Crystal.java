package me.puyodead.enchantcrystals;

import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Crystal {

    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount = 1;
    private ItemStack itemStack = null;

    public Crystal() {
    }

    public Crystal addEnchantment(final Enchantment e, final int level) {
        this.enchantments.put(e, level);

        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Crystal setAmount(int amount) {
        this.amount = amount;

        return this;
    }

    public Crystal build() {
        final ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")), this.enchantments.size())));
        List<String> loreList = new ArrayList<>();

        for (final Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            loreList.add(EnchantCrystalsUtils.colorize(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.enchant lore")).replace("%ENCHANT_NAME%", EnchantCrystalsUtils.translateEnchantmentName(entry.getKey())).replace("%ENCHANT_LEVEL%", EnchantCrystalsUtils.translateEnchantmentLevel(entry.getValue()))));
        }

        loreList.addAll(EnchantCrystalsUtils.colorList(EnchantCrystals.plugin.getConfig().getStringList("settings.crystal.lore")));

        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);

        final NBTItem nbti = new NBTItem(itemStack);
        nbti.setBoolean("enchantcrystals:isEnchantCrystal", true);

        final NBTCompoundList nbtList = nbti.getCompoundList("StoredEnchants");

        for (final Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            final NBTListCompound listCompound = nbtList.addCompound();
            listCompound.setString("id", entry.getKey().getKey().toString());
            listCompound.setInteger("lvl", entry.getValue());

        }

        this.itemStack = nbti.getItem();
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

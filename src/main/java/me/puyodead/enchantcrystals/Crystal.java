package me.puyodead.enchantcrystals;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Crystal {

    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private int amount = 1;
    private ItemStack itemStack = null;

    public Crystal addEnchantment(final Enchantment e, final int level) {
        this.enchantments.put(e, level);

        return this;
    }

    public Crystal setAmount(int amount) {
        this.amount = amount;

        return this;
    }

    public Crystal build() {
        Material material = Material.matchMaterial(EnchantCrystals.plugin.getConfig().getString("settings.item.material"));
        
        // if the material isn't found, print to console and use nether star
        if (Objects.isNull(material)) {
            System.out.println("[WARN]: Attempted to match material but got null! Make sure you are using the right material name! Falling back to Nether Star");
            material = Material.NETHER_STAR;
        }

        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceDisplayName(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.item.display_name")), this.enchantments.size())));

        for (final Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            itemMeta.addEnchant(entry.getKey(), entry.getValue(), false);
        }

        List<String> loreList = new ArrayList<>(EnchantCrystalsUtils.colorList(EnchantCrystals.plugin.getConfig().getStringList("settings.item.lore")));

        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(this.amount);

        final NBTItem nbti = new NBTItem(itemStack);
        nbti.setBoolean("enchantcrystals:isEnchantCrystal", true);

        this.itemStack = nbti.getItem();
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        for (final Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            this.addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    public void removeEnchantment(final Enchantment enchantment) {
        this.enchantments.remove(enchantment);
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }
}

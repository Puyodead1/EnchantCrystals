package me.puyodead1.enchantcrystals;

import me.puyodead1.enchantcrystals.hooks.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnchantCrystal {
    private final HashMap<Enchantment, Integer> enchantments;
    private int amount;
    private ItemStack itemStack;
    private Material material;

    public EnchantCrystal(final Enchantment baseEnchantment, final int enchantLevel, final int amount) {
        this.enchantments = new HashMap<>();
        this.amount = amount;

        // apply the base enchant
        this.addEnchantment(baseEnchantment, enchantLevel);

        material = Material.matchMaterial(EnchantCrystals.getPlugin().getConfig().getString("item.material"));

        if (Objects.isNull(material)) {
            EnchantCrystals.getPlugin().getLogger().severe(String.format("Failed to load Crystal Material %s! Falling back to Nether Star."));
            material = Material.NETHER_STAR;
        }

        itemStack = new ItemStack(material);

        // add item meta
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        final String itemDisplayName = EnchantCrystals.getPlugin().getConfig().getString("item.display_name");
        itemMeta.setDisplayName(Utils.colorize(Utils.replaceDisplayName(itemDisplayName, enchantments.size())));

        this.itemStack.setItemMeta(itemMeta);

        updateItemStack();
    }

    public void addEnchantment(final Enchantment e, final int level) {
        this.enchantments.put(e, level);
    }

    public EnchantCrystal updateItemStack() {
        // update ItemStack
        ItemStack tmpItem = this.itemStack;
        for(final Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            // apply an enchantment to the ItemStack
            tmpItem = entry.getKey().apply(itemStack, entry.getValue());
        }

        this.itemStack = tmpItem;

        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }
}

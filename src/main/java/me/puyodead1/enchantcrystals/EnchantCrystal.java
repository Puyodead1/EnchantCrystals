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
    private int amount = 1;
    private ItemStack itemStack;
    private Material material;

    public EnchantCrystal() {
        this.enchantments = new HashMap<>();

        material = Material.matchMaterial(EnchantCrystals.getPlugin().getConfig().getString("item.material"));

        if (Objects.isNull(material)) {
            EnchantCrystals.getPlugin().getLogger().severe(String.format("Failed to load Crystal Material %s! Falling back to Nether Star."));
            material = Material.NETHER_STAR;
        }
    }

    public EnchantCrystal(final Enchantment baseEnchantment, final int enchantLevel, final int amount) {
        this.enchantments = new HashMap<>();
        this.amount = amount;

        material = Material.matchMaterial(EnchantCrystals.getPlugin().getConfig().getString("item.material"));

        if (Objects.isNull(material)) {
            EnchantCrystals.getPlugin().getLogger().severe(String.format("Failed to load Crystal Material %s! Falling back to Nether Star."));
            material = Material.NETHER_STAR;
        }

        addEnchantment(baseEnchantment, enchantLevel);
    }

    public void addEnchantment(final Enchantment e, final int level) {
        enchantments.put(e, level);
        updateItemStack();
    }

    public void addEnchantments(final HashMap<Enchantment, Integer> enchants) {
        enchantments.putAll(enchants);
        updateItemStack();
    }

    public void updateItemStack() {
        // create new itemstack
        itemStack = new ItemStack(material);

        // set display name
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final String itemDisplayName = EnchantCrystals.getPlugin().getConfig().getString("item.display_name");
        itemMeta.setDisplayName(Utils.colorize(Utils.replaceDisplayName(itemDisplayName, enchantments.size())));

        // set the meta
        itemStack.setItemMeta(itemMeta);

        // add enchants
        for (final Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            System.out.println(entry.getKey().getKey());
            // apply an enchantment to the ItemStack, use unsafe only for the crystal item
            itemStack = entry.getKey().applyUnsafe(itemStack, entry.getValue());
        }
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

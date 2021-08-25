package me.puyodead1.enchantcrystals.events;

import me.puyodead1.enchantcrystals.Crystal;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class OpenInventoryEvent implements Listener {

    @EventHandler
    /**
     * This is for modifying merchant trades to change enchant books to enchant crystals
     */
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (!EnchantCrystals.getPlugin().getConfig().getBoolean("settings.merchants.enabled")) return;
        if (e.isCancelled()) return;
        // ensure its a merchant
        if (!e.getInventory().getType().equals(InventoryType.MERCHANT)) return;

        final MerchantInventory merchantInventory = (MerchantInventory) e.getView().getTopInventory();

        for (int i = 0; i < merchantInventory.getMerchant().getRecipes().size(); i++) {
            final MerchantRecipe merchantRecipe = merchantInventory.getMerchant().getRecipe(i);
            final ItemStack originalResult = merchantRecipe.getResult();

            // Check if the item is an enchanted book
            if (!originalResult.getType().equals(Material.ENCHANTED_BOOK)) continue;
            final EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) originalResult.getItemMeta();

            // Check if book has stored enchants
            if (!itemMeta.hasStoredEnchants()) continue;

            final Crystal crystal = new Crystal();

            // loop the existing enchants and add them to the crystal
            for (final Map.Entry<Enchantment, Integer> entry : itemMeta.getStoredEnchants().entrySet()) {
                crystal.addEnchantment(entry.getKey(), entry.getValue());
            }

            final MerchantRecipe newRecipe = new MerchantRecipe(crystal.build().getItemStack(), merchantRecipe.getMaxUses());
            newRecipe.setIngredients(merchantRecipe.getIngredients());

            // add it
            merchantInventory.getMerchant().setRecipe(i, newRecipe);
        }
    }
}

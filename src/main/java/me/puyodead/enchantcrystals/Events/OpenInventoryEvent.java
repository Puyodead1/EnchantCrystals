package me.puyodead.enchantcrystals.Events;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import me.puyodead.enchantcrystals.Crystal;
import me.puyodead.enchantcrystals.CrystalType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.Objects;

public class OpenInventoryEvent implements Listener {

    @EventHandler
    /**
     * This is for modifying merchant trades to change enchant books to enchant crystals
     */
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.isCancelled()) return;
        // ensure its a merchant
        if (!e.getInventory().getType().equals(InventoryType.MERCHANT)) return;

        final MerchantInventory merchantInventory = (MerchantInventory) e.getView().getTopInventory();

        for (int i = 0; i < merchantInventory.getMerchant().getRecipes().size(); i++) {
            final MerchantRecipe merchantRecipe = merchantInventory.getMerchant().getRecipe(i);
            final ItemStack originalResult = merchantRecipe.getResult();

            if (!originalResult.getType().equals(Material.ENCHANTED_BOOK)) continue;

            NBTItem nbti = new NBTItem(originalResult);
            NBTCompoundList list = nbti.getCompoundList("StoredEnchantments");
            NBTListCompound listCompound = list.get(0);
            final short level = listCompound.getShort("lvl");
            final String key = listCompound.getString("id");

            // incase we ever get to multiple enchants, use this
//            for (NBTListCompound listCompound : list) {
//                final short level = listCompound.getShort("lvl");
//                final String key = listCompound.getString("id");
//                System.out.println(level + ";" + key);
//            }

            final CrystalType crystalType = CrystalType.valueOf(NamespacedKey.fromString(key));
            if (Objects.isNull(crystalType)) continue;
            final Crystal crystal = new Crystal(crystalType, level);
            final MerchantRecipe newRecipe = new MerchantRecipe(crystal.getItemStack(), merchantRecipe.getMaxUses());
            newRecipe.setIngredients(merchantRecipe.getIngredients());
            // add it
            merchantInventory.getMerchant().setRecipe(i, newRecipe);
        }
    }
}

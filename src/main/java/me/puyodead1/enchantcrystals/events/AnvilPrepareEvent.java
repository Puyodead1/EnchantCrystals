package me.puyodead1.enchantcrystals.events;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.puyodead1.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class AnvilPrepareEvent implements Listener {

//    private EnchantCrystals plugin;
//
//    public AnvilPrepareEvent(EnchantCrystals plugin) {
//        this.plugin = plugin;
//    }

    @EventHandler
    public void onPreapreAnvilEvent(PrepareAnvilEvent e) {
        final AnvilInventory inventory = e.getInventory();
        final ItemStack item1 = inventory.getItem(0);
        final ItemStack item2 = inventory.getItem(1);
        final ItemStack result = e.getResult();

        // require 2 items
        if (Objects.isNull(item1) || Objects.isNull(item2)) return;

        // return if one item is air
        if (item1.getType().equals(Material.AIR) || item2.getType().equals(Material.AIR)) return;

        final NBTItem nbti1 = new NBTItem(item1);
        final NBTItem nbti2 = new NBTItem(item2);

        // require both items to be a valid crystal
        if (!EnchantCrystalsUtils.isCrystal(nbti1) && !EnchantCrystalsUtils.isCrystal(nbti2)) return;

//
//        final NamespacedKey enchantKey1 = NamespacedKey.fromString(nbti1.getString("enchantmentkey"));
//        final NamespacedKey enchantKey2 = NamespacedKey.fromString(nbti2.getString("enchantmentkey"));
//
//        // return of either of the keys is null
//        if (enchantKey1 == null || enchantKey2 == null) return;
//
//        // enchants aren't the same
//        if (!enchantKey1.equals(enchantKey2)) return;

//        final CrystalType type1 = CrystalType.valueOf(enchantKey1);
//        final CrystalType type2 = CrystalType.valueOf(enchantKey2);
        //   final int enchantLevel1 = nbti
        //e.setResult(new ItemStack(Material.NETHER_STAR));
    }
}

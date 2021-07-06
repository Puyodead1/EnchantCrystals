package me.puyodead.enchantcrystals.Events;

import me.puyodead.enchantcrystals.EnchantCrystals;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemEnchantEvent implements Listener {

    private EnchantCrystals plugin;

    public ItemEnchantEvent(EnchantCrystals plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e) {
        if (!EnchantCrystals.getPlugin().getConfig().getBoolean("settings.enchanting_tables.enabled")) return;
        e.setCancelled(true);
        final EnchantingInventory inventory = (EnchantingInventory) e.getInventory();
        final ItemStack primary = inventory.getItem();
        final ItemStack secondary = inventory.getSecondary();

        if (Objects.isNull(primary) || (EnchantCrystals.getPlugin().getConfig().getBoolean("settings.enchanting_tables.require_lapis") && Objects.isNull(secondary)) || !primary.getType().equals(Material.BOOK)) {
            e.setCancelled(false);
            return;
        }

        //final Crystal crystal = new Crystal();

        // add the enchants to the crystal
//        for (final Enchantment enchantment : e.getEnchantsToAdd().keySet()) {
//            final int level = e.getEnchantsToAdd().get(enchantment);
//
//            crystal.addEnchantment(enchantment, level);
//        }

        if (EnchantCrystals.getPlugin().getConfig().getBoolean("settings.enchanting_tables.require_lapis")) {
            // take lapis
            secondary.setAmount(secondary.getAmount() - e.getExpLevelCost());
        }

        // take the book
        e.getInventory().setItem(0, new ItemStack(Material.AIR));

//        // take exp if not in creative
//        if (!e.getEnchanter().getGameMode().equals(GameMode.CREATIVE))
//            e.getEnchanter().setLevel(e.getEnchanter().getLevel() - e.getExpLevelCost());

        // add the crystal to the players inventory
        //  e.getEnchanter().getInventory().addItem(crystal.build().getItemStack());

        // play enchantment sound
        e.getEnchanter().getWorld().playSound(e.getEnchanter().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);

        // TODO: reroll enchants
        try {
            final int cost = e.getEnchanter().getGameMode().equals(GameMode.CREATIVE) ? 0 : e.getExpLevelCost();
            plugin.getNMS().onEnchantmentPerformed(e.getEnchanter(), cost, e.getView());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

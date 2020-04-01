package me.puyodead.enchantcrystals.Events;

import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import me.puyodead.enchantcrystals.CrystalType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CrystalUseEvent implements Listener {

    @EventHandler
    public void CrystalUse(InventoryClickEvent e) {
        final ItemStack item;
        final Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory() != null && (e.getView().getTitle().equals("Crafting") || e.getView().getTitle().equals("container.inventory") || e.getView().getTitle().equals("container.crafting")) && (e.getCursor() != null && e.getCursor().getType().equals(Material.NETHER_STAR)) && (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR))) {
            // currentitem is rabbit_stew
            // cursor is star
            if(e.getCursor().getType().equals(Material.NETHER_STAR) && Objects.requireNonNull(e.getCursor().getItemMeta()).hasDisplayName() && e.getCursor().getItemMeta().getDisplayName().equals(EnchantCrystalsUtils.Color("&6Enchantment Crystal"))) {
                // frst step is validate the cursor (star)
                if(!e.getCurrentItem().getType().equals(Material.AIR) && !e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                    // second step is to check if current item (soup) is valid
                    if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                        final ItemStack current = e.getCurrentItem(), cursor = e.getCursor();
                        final String crystalEnchantName = Objects.requireNonNull(cursor.getItemMeta().getLore()).get(0).split(": ")[1];
                        final CrystalType crystalType = CrystalType.valueOf(crystalEnchantName);
                        final int crystalLevel = Integer.parseInt(cursor.getItemMeta().getLore().get(1).split(": ")[1]);
                        if (crystalType != null && current != null && cursor != null) {
                            if (crystalType.getCrystalEnchantment().canEnchantItem(current)) {
                                final ItemStack result = EnchantCrystalsUtils.addEnchantToItem(current, crystalType.getCrystalEnchantment(), crystalLevel);
                                if (result != null) {
                                    player.getInventory().remove(current);
                                    final int amount = cursor.getAmount();
                                    if (amount == 1) {
                                        e.setCursor(new ItemStack(Material.AIR));
                                    } else {
                                        cursor.setAmount(amount - 1);
                                    }
                                    player.getInventory().addItem(result);
                                } else {
                                    EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.item contains enchant"));
                                }
                            } else {
                                EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.cant enchant item"));
                            }
                        } else {
                            EnchantCrystalsUtils.sendPlayer(player, "CrystalType is null :(");
                        }
                    } else {
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.cant be used in creative"));
                    }
                }
            }
        }
    }
}

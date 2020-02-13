package me.puyodead.cosmiccrystals.Events;

import me.puyodead.cosmiccrystals.CosmicCrystals;
import me.puyodead.cosmiccrystals.CosmicCrystalsUtils;
import me.puyodead.cosmiccrystals.CrystalType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CrystalUseEvent implements Listener {

    @EventHandler
    public void CrystalUse(InventoryClickEvent e) {
        final Player player = e.getWhoClicked() instanceof Player ? (Player)e.getWhoClicked() : null;
        if (player != null && e.getClickedInventory() != null && e.getClickedInventory().getTitle().equals("container.inventory") && e.getCursor() != null && e.getCurrentItem() != null) {
            if (e.getCursor().getType().equals(Material.NETHER_STAR) && !e.getCurrentItem().getType().equals(Material.AIR) && e.getCursor().getItemMeta().hasDisplayName() && !e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                if (e.getCursor().getItemMeta().getDisplayName().equals(CosmicCrystalsUtils.Color(CosmicCrystals.plugin.getConfig().getString("settings.crystal display name")))) {
                    final ItemStack itemToApplyTo = e.getCurrentItem();
                    final ItemStack crystal = e.getCursor();

                    final String crystalEnchantName = crystal.getItemMeta().getLore().get(0).split(": ")[1];

                    final CrystalType crystalType = CrystalType.valueOf(crystalEnchantName);

                    final int crystalLevel = Integer.parseInt(crystal.getItemMeta().getLore().get(1).split(": ")[1]);
                    if (crystalType != null) {
                        //
                        if (crystalType.getCrystalEnchantment().canEnchantItem(itemToApplyTo)) {
                            if (!itemToApplyTo.getEnchantments().containsKey(crystalType.getCrystalEnchantment())) {
                                // can enchant item
                                // CosmicCrystalsUtils.sendPlayer(player, "&aSuccessfully applied " + crystalType.getCrystalEnchantment().getName() + " " + crystalLevel + " to " + itemToApplyTo.getType().name());
                                CosmicCrystalsUtils.sendPlayer(player, CosmicCrystals.plugin.getConfig().getString("messages.success applied").replace("%CRYSTAL_TYPE%", crystalType.getCrystalEnchantment().getName()).replace("%CRYSTAL_LEVEL%", crystalLevel + "").replace("%ITEM_TYPE%", itemToApplyTo.getType().name()));
                                player.setItemOnCursor(null);
                                crystal.setAmount(crystal.getAmount() - 1);
                                player.setItemOnCursor(crystal);
                                player.updateInventory();
                                itemToApplyTo.addEnchantment(crystalType.getCrystalEnchantment(), crystalLevel);
                                e.setCancelled(true);
                            } else {
                                // enchant exists on the item
                                CosmicCrystalsUtils.sendPlayer(player, CosmicCrystals.plugin.getConfig().getString("messages.item contains enchant"));
                            }
                        } else {
                            // cannot enchant item
                            CosmicCrystalsUtils.sendPlayer(player, CosmicCrystals.plugin.getConfig().getString("messages.cant enchant item"));
                        }
                    } else {
                        CosmicCrystalsUtils.sendPlayer(player, "CrystalType is null :(");
                    }
                }
            }
        }
    }
}

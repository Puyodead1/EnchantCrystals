package me.puyodead.enchantcrystals.Events;

import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import me.puyodead.enchantcrystals.CrystalType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CrystalUseEvent implements Listener {

    @EventHandler
    public void CrystalUse(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory() != null && (e.getView().getTitle().equals("Crafting") || e.getView().getTitle().equals("container.inventory")) && (e.getCursor() != null && e.getCursor().getType().equals(Material.NETHER_STAR)) && (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR))) {
            EnchantCrystalsUtils.sendPlayer(player, "&aAll good!");
        }
    }
}

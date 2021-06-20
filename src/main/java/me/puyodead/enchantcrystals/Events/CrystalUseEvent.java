package me.puyodead.enchantcrystals.Events;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.puyodead.enchantcrystals.CrystalType;
import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class CrystalUseEvent implements Listener {

    @EventHandler
    public void CrystalUse(InventoryClickEvent e) {
        e.setCancelled(true);
        final Player player = (Player) e.getWhoClicked();
        final Inventory inventory = e.getClickedInventory();
        final ClickType clickType = e.getClick();
        final ItemStack currentItem = e.getCurrentItem();
        final ItemStack cursorItem = e.getCursor();
        // ensure that nothing is null, and that the items are not air, and that the cursorItem is only a nether star, also ignore stacking crystals
        if (inventory == null || currentItem == null || cursorItem == null || currentItem.getType().equals(Material.AIR) || cursorItem.getType().equals(Material.AIR) || !cursorItem.getType().equals(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material"))) || currentItem.getType().equals(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material")))) {
            e.setCancelled(false);
            return;
        }

        NBTItem nbtCursorItem = new NBTItem(cursorItem);

        // if this NBT doesnt exist, its not a crystal
        if (!nbtCursorItem.hasKey("puyodead1:enchantcrystals")) {
            e.setCancelled(false);
            return;
        }

        // if these are missing, its an invalid crystal somehow
        if (!nbtCursorItem.hasKey("enchantmentkey") || !nbtCursorItem.hasKey("enchantmentlevel")) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            Bukkit.broadcastMessage("Invalid crystal");
            e.setCancelled(false);
            return;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.creative error"));
            e.setCancelled(false);
            return;
        }

        String enchantmentKey = nbtCursorItem.getString("enchantmentkey");
        Integer enchantmentLevel = nbtCursorItem.getInteger("enchantmentlevel");

        NamespacedKey namespacedKey = NamespacedKey.fromString(enchantmentKey);
        Enchantment enchantment = Enchantment.getByKey(namespacedKey);

        if (enchantment == null) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid enchantment"));
            e.setCancelled(false);
            return;
        }

        if (!enchantment.canEnchantItem(currentItem)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid item"));
            e.setCancelled(false);
            return;
        }

        final CrystalType crystalType = CrystalType.valueOf(namespacedKey);
        // remove a crystal if its a stack or just remove the crystal
        cursorItem.setAmount(cursorItem.getAmount() - 1);
        currentItem.addEnchantment(crystalType.getEnchantment(), enchantmentLevel);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.success")), crystalType, enchantmentLevel, currentItem));
    }
}

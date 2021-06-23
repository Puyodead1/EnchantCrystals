package me.puyodead.enchantcrystals.Events;

import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;

public class CrystalUseEvent implements Listener {

    @EventHandler
    public void CrystalUse(InventoryClickEvent e) {
        e.setCancelled(true);
        final Player player = (Player) e.getWhoClicked();
        final Inventory inventory = e.getClickedInventory();
        final ItemStack currentItem = e.getCurrentItem();
        final ItemStack cursorItem = e.getCursor();
        // ensure that nothing is null, and that the items are not air, and that the cursorItem is only a nether star, also ignore stacking crystals
        if (inventory == null || currentItem == null || cursorItem == null || currentItem.getType().equals(Material.AIR) || cursorItem.getType().equals(Material.AIR) || !cursorItem.getType().equals(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material"))) || currentItem.getType().equals(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material")))) {
            e.setCancelled(false);
            return;
        }

        NBTItem nbtCursorItem = new NBTItem(cursorItem);

        if (!EnchantCrystalsUtils.isCrystal(nbtCursorItem)) {
            e.setCancelled(false);
            return;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.creative error"));
            e.setCancelled(false);
            return;
        }

        final NBTCompoundList storedEnchants = nbtCursorItem.getCompoundList("StoredEnchants");

        for (final NBTListCompound listCompound : storedEnchants) {
            final String enchantmentKey = listCompound.getString("id");
            final int enchantmentLevel = listCompound.getInteger("lvl");

            NamespacedKey namespacedKey = NamespacedKey.fromString(enchantmentKey);
            Enchantment enchantment = Enchantment.getByKey(namespacedKey);

            if (enchantment == null) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid enchantment"));
                e.setCancelled(false);
                continue;
            }

            if (!enchantment.canEnchantItem(currentItem)) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid item"));
                e.setCancelled(false);
                continue;
            }

            // Check for conflicting enchants
            for (Map.Entry<Enchantment, Integer> entry : currentItem.getEnchantments().entrySet()) {
                if (entry.getKey().getKey().equals(enchantment.getKey()))
                    continue; // allow the same enchant for stacking
                if (entry.getKey().conflictsWith(enchantment)) {
                    Bukkit.broadcastMessage(EnchantCrystalsUtils.translateEnchantmentName(enchantment) + " conflicts with " + EnchantCrystalsUtils.translateEnchantmentName(entry.getKey()));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    e.setCancelled(false);
                    continue;
                }
            }

            // handle stacking enchants
            if (currentItem.containsEnchantment(enchantment)) {
                int currentEnchantLevel = currentItem.getEnchantmentLevel(enchantment);
                int addedEnchantLevel = currentEnchantLevel + enchantmentLevel;

                // check if the combined enchant level exceeds the enchants max
                if (addedEnchantLevel > enchantment.getMaxLevel()) {
                    // its the same enchant but it has a higher level, apply it
                    if (enchantmentLevel > currentEnchantLevel) {
                        cursorItem.setAmount(cursorItem.getAmount() - 1);
                        currentItem.addEnchantment(enchantment, enchantmentLevel);
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                        Bukkit.broadcastMessage("Replaced level " + currentEnchantLevel + " with level " + enchantmentLevel);
                        continue;
                    }

                    // the enchant level exceeds the enchants max and the level is not higher then the current applied level, exit
                    Bukkit.broadcastMessage("Combining this enchantment would exceed the max level");
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    e.setCancelled(false);
                    continue;
                }

                // combine the enchant levels
                cursorItem.setAmount(cursorItem.getAmount() - 1);
                currentItem.addEnchantment(enchantment, addedEnchantLevel);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                Bukkit.broadcastMessage("Combined");
                continue;
            }

            // remove a crystal if its a stack or just remove the crystal
            cursorItem.setAmount(cursorItem.getAmount() - 1);
            currentItem.addEnchantment(enchantment, enchantmentLevel);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.success")), enchantment, enchantmentLevel, currentItem));
        }

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    }
}

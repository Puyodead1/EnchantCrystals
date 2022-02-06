package me.puyodead1.enchantcrystals.events;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import me.puyodead1.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;

public class CrystalUseEvent implements Listener {

    @EventHandler
    // TODO: Fix NPath complexity
    // TODO: Fix long length
    public void crystalUse(InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        final Inventory inventory = e.getClickedInventory();
        // item to apply to
        final ItemStack currentItem = e.getCurrentItem();
        // crystal
        final ItemStack cursorItem = e.getCursor();
        // crystal material
        final Material crystalMaterial = Material.valueOf(EnchantCrystals.getPlugin().getConfig().getString("settings.item.material"));

        // null checks
        if (Objects.isNull(inventory) || Objects.isNull(currentItem) || Objects.isNull(cursorItem)) {
            return;
        }

        final ItemMeta cursorItemMeta = cursorItem.getItemMeta();

        // ensure that the items are not air, and that the cursorItem is only a nether star, also ignore stacking crystals
        if (Objects.isNull(cursorItemMeta) || currentItem.getType().equals(Material.AIR) || cursorItem.getType().equals(Material.AIR) || !cursorItem.getType().equals(crystalMaterial) || currentItem.getType().equals(crystalMaterial)) {
            return;
        }

        // create nbt item from the cursor item
        final NBTItem nbtCursorItem = new NBTItem(cursorItem);

        // nbt item null check
        if (!EnchantCrystalsUtils.isCrystal(nbtCursorItem)) {
            return;
        }

        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.getPlugin().getConfig().getString("messages.creative_error"));
            return;
        }

        if (!cursorItemMeta.hasEnchants()) {
            // crystal has no enchants on it, how did we get here?!
            EnchantCrystalsUtils.sendPlayer(player, "Crystal does not contain any enchants, Please report this to the developer and include how you managed to do this! x_x");
            return;
        }

        final Map<Enchantment, Integer> enchants = cursorItemMeta.getEnchants();

        // handle a crystal with only one enchantment
        if (enchants.size() == 1) {
            final ItemStack clone = cursorItem.clone();
            clone.setAmount(1);

            final Map.Entry<Enchantment, Integer> entry = enchants.entrySet().iterator().next();
            final int cursorEnchantLevel = clone.getEnchantmentLevel(entry.getKey());

            // check if enchant can be applied to the item
            if (!entry.getKey().canEnchantItem(currentItem)) {
                rejectInvalidItem(player, currentItem, entry.getKey());
                return;
            }

            // Check for conflicting enchants
            for (Map.Entry<Enchantment, Integer> currentEntry : currentItem.getEnchantments().entrySet()) {
                if (currentItem.containsEnchantment(entry.getKey()))
                    continue; // allow the same enchant for stacking

                // check if enchant conflicts with any of the existing enchants
                if (currentEntry.getKey().conflictsWith(entry.getKey())) {
                    refuseConflict(player, entry.getKey(), currentEntry);
                    return;
                }
            }

            // handle stacking
            if (currentItem.containsEnchantment(entry.getKey())) {
                final int currentEnchantLevel = currentItem.getEnchantmentLevel(entry.getKey());
                final int addedEnchantLevel = currentEnchantLevel + cursorEnchantLevel;
                if (addedEnchantLevel > entry.getKey().getMaxLevel()) {
                    // it's the same enchant, and it has a higher level, but it would exceed the enchants maximum, so we can't combine the levels and should just add the max
                    if (cursorEnchantLevel > currentEnchantLevel) {
                        e.setCancelled(true);
                        applyReplace(player, currentItem, clone, entry.getKey(), cursorEnchantLevel);
                        cursorItem.setAmount(cursorItem.getAmount() - 1);
                        return;
                    }

                    // enchant level exceeds the enchants max and the level is not higher than the current applied level, exit
                    e.setCancelled(true);
                    rejectExceed(player, entry.getKey());
                    return;
                }

                // combine enchant levels
                e.setCancelled(true);
                applyUpgrade(player, currentItem, clone, entry.getKey(), currentEnchantLevel, addedEnchantLevel);
                cursorItem.setAmount(cursorItem.getAmount() - 1);
                return;
            }

            e.setCancelled(true);
            apply(player, currentItem, clone, entry.getKey(), cursorEnchantLevel);
            cursorItem.setAmount(cursorItem.getAmount() - 1);
        } else {
            final ItemStack clone = cursorItem.clone();
            clone.setAmount(1);

            final int enchantAmount = clone.getEnchantments().size();

            // handle a crystal with more than one enchantment
            for (final Enchantment enchantment : clone.getEnchantments().keySet()) {
                // check if enchant can be applied to the item
                if (!enchantment.canEnchantItem(currentItem)) {
                    e.setCancelled(true);
                    rejectInvalidItem(player, currentItem, enchantment);
                    continue;
                }

                // Check for conflicting enchants
                for (Map.Entry<Enchantment, Integer> currentEntry : currentItem.getEnchantments().entrySet()) {
                    if (currentItem.containsEnchantment(enchantment)) {
                        continue; // allow the same enchant for stacking
                    }
                    // check if enchant conflicts with any of the existing enchants
                    if (currentEntry.getKey().conflictsWith(enchantment)) {
                        e.setCancelled(true);
                        refuseConflict(player, enchantment, currentEntry);
                    }
                }

                // handle stacking
                if (currentItem.containsEnchantment(enchantment)) {
                    int currentEnchantLevel = currentItem.getEnchantmentLevel(enchantment);
                    int addedEnchantLevel = currentEnchantLevel + clone.getEnchantmentLevel(enchantment);

                    if (addedEnchantLevel > enchantment.getMaxLevel()) {
                        // its the same enchant but it has a higher level, apply it
                        if (clone.getEnchantmentLevel(enchantment) > currentEnchantLevel) {
                            e.setCancelled(true);
                            applyReplace(player, currentItem, clone, enchantment, clone.getEnchantmentLevel(enchantment));
                            continue;
                        }

                        // the enchant level exceeds the enchants max and the level is not higher then the current applied level, exit
                        e.setCancelled(true);
                        rejectExceed(player, enchantment);
                        continue;
                    }

                    // combine enchant levels
                    e.setCancelled(true);
                    applyUpgrade(player, currentItem, clone, enchantment, currentEnchantLevel, addedEnchantLevel);
                    continue;
                }

                // remove a crystal if it's a stack or just remove the crystal
                e.setCancelled(true);
                apply(player, currentItem, clone, enchantment, clone.getEnchantmentLevel(enchantment));
            }

            if (clone.getEnchantments().size() != enchantAmount) {
                e.setCancelled(true);
                cursorItem.setAmount(cursorItem.getAmount() - 1);
            }
        }
    }

    private void rejectInvalidItem(final Player player, final ItemStack currentItem, final Enchantment enchantment) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceInvalid(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_item")), enchantment, currentItem)));
    }

    private void rejectExceed(final Player player, final Enchantment enchantment) {
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceExceed(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_max_exceed")), enchantment)));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }

    private void refuseConflict(final Player player, final Enchantment enchantment, final Map.Entry<Enchantment, Integer> currentEntry) {
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceConflicting(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_conflict")), enchantment, currentEntry.getKey())));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }

    private void apply(final Player player, final ItemStack currentItem, final ItemStack cursorItem, final Enchantment enchantment, final int cursorEnchantLevel) {
        currentItem.addEnchantment(enchantment, cursorEnchantLevel);
        cursorItem.removeEnchantment(enchantment);

        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_success")), enchantment, cursorEnchantLevel, currentItem));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
    }

    private void applyUpgrade(final Player player, final ItemStack currentItem, final ItemStack cursorItem, final Enchantment enchantment, final int currentEnchantLevel, final int addedEnchantLevel) {
        currentItem.addEnchantment(enchantment, addedEnchantLevel);
        cursorItem.removeEnchantment(enchantment);

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.replaceUpgraded(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_upgraded")), enchantment, currentEnchantLevel, addedEnchantLevel));
    }

    private void applyReplace(final Player player, final ItemStack currentItem, final ItemStack cursorItem, final Enchantment enchantment, final int cursorEnchantLevel) {
        currentItem.addEnchantment(enchantment, cursorItem.getEnchantmentLevel(enchantment));
        cursorItem.removeEnchantment(enchantment);

        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_success")), enchantment, cursorEnchantLevel, currentItem));
    }
}

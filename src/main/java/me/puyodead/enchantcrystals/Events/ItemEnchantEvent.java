package me.puyodead.enchantcrystals.Events;

import me.puyodead.enchantcrystals.Crystal;
import me.puyodead.enchantcrystals.CrystalType;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import me.puyodead.enchantcrystals.ReflectionUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Objects;

public class ItemEnchantEvent implements Listener {

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e) {
        e.setCancelled(true);
        final EnchantingInventory inventory = (EnchantingInventory) e.getInventory();
        final ItemStack primary = inventory.getItem();
        final ItemStack secondary = inventory.getSecondary();

        if (Objects.isNull(primary) || Objects.isNull(secondary) || !primary.getType().equals(Material.BOOK)) {
            e.setCancelled(false);
            return;
        }

        // process the crystals
        for (final Enchantment enchantment : e.getEnchantsToAdd().keySet()) {
            final int level = e.getEnchantsToAdd().get(enchantment);

            final CrystalType crystalType = CrystalType.valueOf(enchantment.getKey());

            if (Objects.isNull(crystalType)) {
                EnchantCrystalsUtils.sendPlayer(e.getEnchanter(), "&cAn error occurred!");
                e.setCancelled(false);
                return;
            }

            final Crystal crystal = new Crystal(crystalType, level, 1);
            e.getEnchanter().getInventory().addItem(crystal.getItemStack());
        }

        // take lapiz
        secondary.setAmount(secondary.getAmount() - e.getExpLevelCost());

        // take the book
        e.getInventory().setItem(0, new ItemStack(Material.AIR));

        // take exp if not in creative
        if (!e.getEnchanter().getGameMode().equals(GameMode.CREATIVE))
            e.getEnchanter().setLevel(e.getEnchanter().getLevel() - e.getExpLevelCost());

        // play enchantment sound
        e.getEnchanter().getWorld().playSound(e.getEnchanter().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);

        try {
            // I really hate NMS >:(
            Class<?> CraftInventoryView = ReflectionUtil.getOBCClass("inventory.CraftInventoryView");
            Class<?> CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer");
            Class<?> ItemStack = ReflectionUtil.getNMSClass("ItemStack");
            Class<?> ContainerProperty = ReflectionUtil.getNMSClass("ContainerProperty");

            Object craftPlayer = CraftPlayer.cast(e.getEnchanter());
            Object entityPlayer = ReflectionUtil.getHandle(craftPlayer);
            Object seed = ReflectionUtil.getField(entityPlayer, "bG");
            Object container = CraftInventoryView.cast(e.getView());
            Object containerEnchantTable = ReflectionUtil.getHandle(container);
            ReflectionUtil.invokeMethod(entityPlayer, "enchantDone", new Class[]{ItemStack, int.class}, new Object[]{null, 0});
            Field enchantSlotsField = containerEnchantTable.getClass().getDeclaredField("enchantSlots");
            enchantSlotsField.setAccessible(true);
            Field containerProperty = containerEnchantTable.getClass().getDeclaredField("i");
            containerProperty.setAccessible(true);
            System.out.println(seed);
            ReflectionUtil.invokeMethod(ContainerProperty.cast(containerProperty.get(containerEnchantTable)), "set", new Class[]{int.class}, new Object[]{seed});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

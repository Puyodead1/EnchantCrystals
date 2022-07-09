package me.puyodead1.enchantcrystals.commands;

import me.puyodead1.enchantcrystals.EnchantCrystal;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import me.puyodead1.enchantcrystals.hooks.Enchantment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command cmd, @NotNull final String label, @NotNull final String[] args) {
        if(!(sender instanceof Player)) return false;
        final Player player = (Player)sender;

        final EnchantCrystal ec = new EnchantCrystal();
        final Enchantment e1 = EnchantCrystals.getEnchantment("minecraft:fire_aspect");
        final Enchantment e2 = EnchantCrystals.getEnchantment("ac:smelting");
        final Enchantment e3 = EnchantCrystals.getEnchantment("ac:experience");
        ec.addEnchantment(e1, e1.getMaxLevel());
        ec.addEnchantment(e2, e2.getMaxLevel());
        ec.addEnchantment(e3, e3.getMaxLevel());
        player.getInventory().addItem(ec.getItemStack());

        return true;
    }
}

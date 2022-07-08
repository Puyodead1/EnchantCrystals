package me.puyodead1.enchantcrystals.commands;

import me.puyodead1.enchantcrystals.EnchantCrystal;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import me.puyodead1.enchantcrystals.hooks.Enchantment;
import me.puyodead1.enchantcrystals.hooks.PluginHook;
import net.advancedplugins.ae.api.AEAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) return false;
        final Player player = (Player)sender;
        final Enchantment enchantment = EnchantCrystals.getEnchantmentByName("SMELTING");
        Bukkit.broadcastMessage(enchantment.getName());
        final EnchantCrystal ec = new EnchantCrystal(enchantment, 1, 1);
        player.getInventory().addItem(ec.getItemStack());
        return true;
    }
}

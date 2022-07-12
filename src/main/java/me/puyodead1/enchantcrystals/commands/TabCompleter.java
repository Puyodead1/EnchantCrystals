package me.puyodead1.enchantcrystals.commands;

import me.puyodead1.enchantcrystals.EnchantCrystals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;
        if (!cmd.getName().equalsIgnoreCase("enchantcrystals")) return null;
        if (args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))) {
            return EnchantCrystals.getEnchantmentKeys();
        }

        return null;
    }
}

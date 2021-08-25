package me.puyodead1.enchantcrystals.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompletion implements TabCompleter {
    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("enchantcrystals") && sender instanceof Player) {
            if (args.length == 1) {
                return new ArrayList<>(Arrays.asList("enchants", "give", "add"));
            } else if (args.length == 2 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))) {
                // enchants
                final List<String> enchantList = new ArrayList<>();

                for (final Enchantment e : EnchantmentWrapper.values()) {
                    enchantList.add(e.getKey().getKey());
                }
                return enchantList;
            } else if (args.length == 3 && (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))) {
                // level
                final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
                if (enchantment != null) {
                    final List<String> levelList = new ArrayList<>();

                    if (enchantment.getStartLevel() == enchantment.getMaxLevel()) {
                        levelList.add(String.valueOf(enchantment.getMaxLevel()));
                        return levelList;
                    }

                    for (int i = enchantment.getStartLevel(); i < enchantment.getMaxLevel() + 1; i++) {
                        levelList.add(String.valueOf(i));
                    }
                    return levelList;
                }
            }
        }
        return null;
    }
}

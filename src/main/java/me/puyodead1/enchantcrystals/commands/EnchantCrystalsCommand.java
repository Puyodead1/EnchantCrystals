package me.puyodead1.enchantcrystals.commands;

import me.puyodead1.enchantcrystals.EnchantCrystals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static me.puyodead1.enchantcrystals.Utils.sendSender;

public class EnchantCrystalsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        final int l = args.length;

        if (l == 0) {
            // print help
            final List<String> list = Arrays.asList(
                    "&6EnchantCrystals v" + EnchantCrystals.getPlugin().getDescription().getVersion() + " by Puyodead1",
                    "&6=============================",
                    "&e/ecr enchants",
                    "&e/ecr give <enchant name>",
                    "&e/ecr give <enchant name> <level>",
                    "&e/ecr give <enchant name> <level> <amount>",
                    "&e/ecr give <enchant name> <level> <amount> <player>",
                    "&e/ecr add <enchant name>",
                    "&e/ecr add <enchant name> <level>",
                    "",
                    "&e&oIf an amount is not specified, 1 is default",
                    "&e&oIf a level is not specified, 1 is default",
                    "&e&oIf a player is not specified, the current player is default");
            sendSender(sender, list);
            return true;
        }

        return false;
    }
}

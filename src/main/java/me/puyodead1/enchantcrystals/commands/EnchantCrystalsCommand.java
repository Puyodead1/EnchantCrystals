package me.puyodead1.enchantcrystals.commands;

import me.puyodead1.enchantcrystals.EnchantCrystal;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import me.puyodead1.enchantcrystals.hooks.Enchantment;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.puyodead1.enchantcrystals.Utils.colorize;
import static me.puyodead1.enchantcrystals.Utils.sendSender;

public class EnchantCrystalsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        final int l = args.length;

        final Player player = (Player) sender;

        if (l == 0) {
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

        final List<String> argss = new ArrayList<>(Arrays.asList(args));
        final String cmdName = argss.get(0).toLowerCase();
        argss.remove(0);

        switch (cmdName) {
            case "enchants":
                sendSender(sender, EnchantCrystals.getEnchantmentNames());
                return true;
            case "give":
                return processGiveCommand(player, argss);
            case "add":
//                if (!(sender instanceof Player)) {
//                    sendSender(sender, EnchantCrystalsUtils.colorize("&4&lSorry, only players can use this command."));
//                    return true;
//                }
//                return processAddCommand(player, argss);
                // TODO: we need to store the enchantments on the item, probably in a Persistant Container, and create an EnchantCrystal instance from the existing item stack
                sendSender(player, "WIP");
                return true;
        }
        return false;
    }


    private boolean processGiveCommand(final CommandSender sender, final List<String> args) {
        final int l = args.size();

        if (l == 0) {
            sendSender(sender, colorize("&e/ecr give <enchant key> [level] [amount] [player]"));
            return true;
        } else if (l == 1) {
            if (!(sender instanceof Player)) {
                sendSender(sender, colorize("&4Sorry, only players can use this command."));
                return true;
            }
            final String enchantmentName = args.get(0).toLowerCase();
            return giveCrystal((Player) sender, enchantmentName, 1, 1);
        } else if (l == 2) {
            if (!(sender instanceof Player)) {
                sendSender(sender, colorize("&4Sorry, only players can use this command."));
                return true;
            }
            //player specified enchant name and level
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            return giveCrystal((Player) sender, enchantmentName, level, 1);
        } else if (l == 3) {
            if (!(sender instanceof Player)) {
                sendSender(sender, colorize("&4Sorry, only players can use this command."));
                return true;
            }
            // player specified enchant name, level, and amount
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            final int amount = Integer.parseInt(args.get(2));
            return giveCrystal((Player) sender, enchantmentName, level, amount);
        } else if (l == 4) {
            // player specified enchant name, level, amount, and player to give to
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            final int amount = Integer.parseInt(args.get(2));
            final Player otherPlayer = Bukkit.getPlayer(args.get(3));
            if (Objects.isNull(otherPlayer)) {
                sendSender(sender, EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_player"));
                return true;
            }
            return giveCrystal(sender, enchantmentName, level, amount, otherPlayer);
        }
        return false;
    }

    private boolean processAddCommand(final Player player, final List<String> args) {
        final int l = args.size();
        System.out.println(l);
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        final int slot = player.getInventory().getHeldItemSlot();
        System.out.println(slot);

        if (l == 0) {
            sendSender(player, colorize("&e/ecr add <enchant key> [level]"));
            System.out.println("l 0");
            return true;
        } else if (l == 1) {
            System.out.println("l 1");
            final String enchantmentName = args.get(0).toLowerCase();
            return addEnchantmentToItem(player, enchantmentName, 1, itemStack, slot);
        } else if (l == 2) {
            System.out.println("l 2");
            //player specified enchant name and level
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            return addEnchantmentToItem(player, enchantmentName, level, itemStack, slot);
        }
        return false;
    }

    private boolean addEnchantmentToItem(final Player player, final String enchantmentName, final int level, final ItemStack itemStack, final int slot) {
        System.out.printf("%s %s %s %s %s%n", player, enchantmentName, level, itemStack, slot);
        final Enchantment enchantment = EnchantCrystals.getEnchantment(enchantmentName);
        if (Objects.isNull(enchantment)) {
            sendSender(player, "enchantment is null");
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            sendSender(player, "level > max");
            return true;
        }

        final ItemStack newItemStack = enchantment.applyUnsafe(itemStack, level);
        player.getInventory().setItem(slot, newItemStack);
        sendSender(player, String.format("added %s", enchantmentName));
        return true;
    }

    private boolean giveCrystal(final Player player, final String enchantmentName, final int level, final int amount) {
        final Enchantment enchantment = EnchantCrystals.getEnchantment(enchantmentName);
        if (Objects.isNull(enchantment)) {
            sendSender(player, "enchantment is null");
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            sendSender(player, "level > max");
            return true;
        }

        final EnchantCrystal enchantCrystal = new EnchantCrystal(enchantment, level, amount);
        final ItemStack itemStack = enchantCrystal.getItemStack();
        player.getInventory().addItem(itemStack);
        sendSender(player, "crystal given");
        return true;
    }

    private boolean giveCrystal(final CommandSender sender, final String enchantmentName, final int level, final int amount, final Player player) {
        final Enchantment enchantment = EnchantCrystals.getEnchantment(enchantmentName);
        if (Objects.isNull(enchantment)) {
            sendSender(player, "enchantment is null");
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            sendSender(player, "level > max");
            return true;
        }

        final EnchantCrystal enchantCrystal = new EnchantCrystal(enchantment, level, amount);
        final ItemStack itemStack = enchantCrystal.getItemStack();
        player.getInventory().addItem(itemStack);
        sendSender(player, "given a crystal");
        sendSender(sender, "crystal given to player");
        return true;
    }
}

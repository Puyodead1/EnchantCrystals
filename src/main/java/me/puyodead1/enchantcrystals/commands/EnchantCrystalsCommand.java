package me.puyodead1.enchantcrystals.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.puyodead1.enchantcrystals.Crystal;
import me.puyodead1.enchantcrystals.EnchantCrystals;
import me.puyodead1.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnchantCrystalsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        final Player player = sender instanceof Player ? (Player) sender : null;
        if (player == null) return true;

        // missing permission
        if (!player.hasPermission("enchantcrystals.enchantcrystals")) {
            EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.getPlugin().getConfig().getString("messages.missing_permission"));
            return true;
        }

        // argument length
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
            EnchantCrystalsUtils.sendSenderList(sender, list);
            return true;
        } else {
            final List<String> argss = new ArrayList<>(Arrays.asList(args));
            final String cmdName = argss.get(0).toLowerCase();
            argss.remove(0);

            switch (cmdName) {
                case "enchants":
                    for (final Enchantment enchantment : EnchantmentWrapper.values()) {
                        // TODO: config
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize("&e&l" + EnchantCrystalsUtils.translateEnchantmentName(enchantment) + "&7: " + enchantment.getKey().getKey()));
                    }
                    return true;
                case "give":
                    return processGiveCommand(player, argss);
                case "add":
                    return processAddCommand(player, argss);
                default:
                    break;
            }
        }
        return false;
    }

    private boolean processGiveCommand(Player player, List<String> args) {
        final int l = args.size();

        if (l == 0) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize("&e/ecr give <enchant key> [level] [amount] [player]"));
            return true;
        } else if (l == 1) {
            final String enchantmentName = args.get(0).toLowerCase();
            return giveCrystal(player, enchantmentName, 1, 1);
        } else if (l == 2) {
            //player specified enchant name and level
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            return giveCrystal(player, enchantmentName, level, 1);
        } else if (l == 3) {
            // player specified enchant name, level, and amount
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            final int amount = Integer.parseInt(args.get(2));
            return giveCrystal(player, enchantmentName, level, amount);
        } else if (l == 4) {
            // player specified enchant name, level, amount, and player to give to
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            final int amount = Integer.parseInt(args.get(2));
            final Player player1 = Bukkit.getPlayer(args.get(3));
            if (Objects.isNull(player1)) {
                EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_player"));
                return true;
            }
            return giveCrystal(player, enchantmentName, level, amount, player1);
        }
        return false;
    }

    private boolean processAddCommand(Player player, List<String> args) {
        final int l = args.size();
        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        final int slot = player.getInventory().getHeldItemSlot();

        if (l == 0) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize("&e/ecr add <enchant key> [level]"));
            return true;
        } else if (l == 1) {
            final String enchantmentName = args.get(0).toLowerCase();
            return addEnchant(player, enchantmentName, 1, itemStack, slot);
        } else if (l == 2) {
            //player specified enchant name and level
            final String enchantmentName = args.get(0).toLowerCase();
            final int level = Integer.parseInt(args.get(1));
            return addEnchant(player, enchantmentName, level, itemStack, slot);
        }
        return false;
    }

    private boolean addEnchant(final Player player, final String enchantmentName, final int level, final ItemStack itemStack, final int slot) {
        if (Objects.isNull(itemStack) || itemStack.getType().equals(Material.AIR) || !EnchantCrystalsUtils.isCrystal(new NBTItem(itemStack))) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystals.getPlugin().getConfig().getString("messages.add_invalid_item")));
            return true;
        }

        final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName));
        if (Objects.isNull(enchantment)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_enchantment")));
            return true;
        }

        final Crystal crystal = new Crystal();
        crystal.setAmount(itemStack.getAmount());

        // add existing enchants
        crystal.addEnchantments(itemStack.getEnchantments());

        // add the new enchant
        crystal.addEnchantment(enchantment, level);
        player.getInventory().setItem(slot, crystal.build().getItemStack());
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceAdd(EnchantCrystals.getPlugin().getConfig().getString("messages.add_success"), enchantment, level)));
        return true;
    }

    private Enchantment getEnchantmentByKey(String key) {
        return Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
    }

    // Sends the success message to the player
    private void sendGiveSuccessMessage(Player player, Enchantment enchantment, int level, int amount) {
        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(EnchantCrystalsUtils.replaceSuccess(EnchantCrystals.getPlugin().getConfig().getString("messages.crystal_given"), enchantment, level, amount)));
    }

    // Creates and returns a new Crystal instance with specified enchantment
    private Crystal makeCrystal(Enchantment enchantment, int level, int amount) {
        if (Objects.isNull(enchantment)) {
            return null;
        }
        return new Crystal().addEnchantment(enchantment, level).setAmount(amount);
    }

    // Enchantment name, amount, and level were specified
    public boolean giveCrystal(Player player, String enchantmentName, int level, int amount) {
        final Enchantment enchantment = getEnchantmentByKey(enchantmentName);
        if (Objects.isNull(enchantment)) return false;
        final Crystal crystal = makeCrystal(enchantment, level, amount);

        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_enchantment"));
            return true;
        }

        // check bounds
        if (level > enchantment.getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_bounds_error")).replace("%ENCHANTMENT_LEVEL_START%", String.valueOf(enchantment.getStartLevel()).replace("%ENCHANTMENT_LEVEL_MAX%", String.valueOf(enchantment.getMaxLevel())))));
            return true;
        }

        player.getInventory().addItem(crystal.build().getItemStack());
        sendGiveSuccessMessage(player, enchantment, level, amount);
        return true;
    }

    // Enchantment name, amount, level, and a player were specified
    public boolean giveCrystal(Player sender, String enchantmentName, int level, int amount, Player player) {
        final Enchantment enchantment = getEnchantmentByKey(enchantmentName);
        if (Objects.isNull(enchantment)) return false;
        final Crystal crystal = makeCrystal(enchantment, level, amount);

        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.getPlugin().getConfig().getString("messages.invalid_enchantment"));
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystalsUtils.colorize(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_bounds_error")).replace("%ENCHANTMENT_LEVEL_START%", String.valueOf(enchantment.getStartLevel()).replace("%ENCHANTMENT_LEVEL_MAX%", String.valueOf(enchantment.getMaxLevel())))));
            return true;
        }

        player.getInventory().addItem(crystal.build().getItemStack());
        EnchantCrystalsUtils.sendSender(sender, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.crystal_given_to_player")), enchantment, amount, level, player));
        EnchantCrystalsUtils.sendSender(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.getPlugin().getConfig().getString("messages.crystal_received")), enchantment, amount, level, player));
        return true;
    }
}

package me.puyodead.enchantcrystals.Commands;

import me.puyodead.enchantcrystals.Crystal;
import me.puyodead.enchantcrystals.CrystalType;
import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GiveCrystalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        final Player player = sender instanceof Player ? (Player) sender : null;
        if (player == null) return true;

        // missing permission
        if (!player.hasPermission("enchantcrystals.givecrystal")) {
            EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.no permission"));
            return true;
        }

        // argument length
        final int l = args.length;

        if (l == 0) {
            final List<String> list = Arrays.asList("&6EnchantCrystals v" + EnchantCrystals.plugin.getDescription().getVersion() + " by Puyodead1", "&6=============================", "&7/gc <enchant name>", "&7/gc <enchant name> <amount>", "&7/gc <enchant name> <amount> <level>", "&7/gc <enchant name> <amount> <level> <player>", "&7If an amount is not specified, one is default", "&7If a level is not specified, max is default", "&7If a player is not specified, current player is default");
            EnchantCrystalsUtils.sendSenderList(sender, list);
            return true;
        } else if (l == 1) {
            // player specified one argument, aka a crystal name
            final String enchantmentName = args[0].toLowerCase();
            return giveCrystal(player, enchantmentName);
        } else if (l == 2) {
            //player specified crystal name and amount
            final String enchantmentName = args[0].toLowerCase();
            final int amount = Integer.parseInt(args[1]);
            return giveCrystal(player, enchantmentName, amount);
        } else if (l == 3) {
            // player specified crystal name, amount, and the level
            final String enchantmentName = args[0].toLowerCase();
            final int amount = Integer.parseInt(args[1]);
            final int level = Integer.parseInt(args[2]);
            return giveCrystal(player, enchantmentName, amount, level);
        } else if (l == 4) {
            // player specified crystal name, amount, the crystal level, the player to give it to
            final String enchantmentName = args[0].toLowerCase();
            final int amount = Integer.parseInt(args[1]);
            final int level = Integer.parseInt(args[2]);
            final Player player1 = Bukkit.getPlayer(args[3]);
            if (Objects.isNull(player1)) {
                EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.invalid player"));
                return true;
            }

            return giveCrystal(player, enchantmentName, amount, level, player1);
        }

        return false;
    }

    private void sendGiveSuccessMessage(Player player, CrystalType type, int amount, int level) {
        EnchantCrystalsUtils.sendPlayer(player,
                Objects.requireNonNull(EnchantCrystalsUtils.replace(
                        Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal given")), type, amount, level, player)));
    }

    private Crystal makeCrystal(String enchantmentName, int amount, int level) {
        final CrystalType crystalType = CrystalType.valueOf(NamespacedKey.minecraft(enchantmentName.toLowerCase()));

        if (Objects.isNull(crystalType)) {
            return null;
        }

        return new Crystal(crystalType, level, amount);
    }

    private boolean ensureCrystalNotNull(Player player, Crystal crystal) {
        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
            return true;
        }

        player.getInventory().addItem(crystal.getItemStack());
        sendGiveSuccessMessage(player, crystal.getType(), crystal.getAmount(), crystal.getLevel());
        return true;
    }

    public boolean giveCrystal(Player player, String enchantmentName) {
        final Crystal crystal = makeCrystal(enchantmentName, 1, 1);
        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player player, String enchantmentName, int amount) {
        final Crystal crystal = makeCrystal(enchantmentName, amount, 1);
        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player player, String enchantmentName, int amount, int level) {
        final Crystal crystal = makeCrystal(enchantmentName, amount, level);

        if (level > crystal.getType().getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, "&cEnchantment level out of bounds! Must be between 1 and " + crystal.getType().getMaxLevel());
            return true;
        }

        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player sender, String enchantmentName, int amount, int level, Player player) {
        final Crystal crystal = makeCrystal(enchantmentName, amount, level);

        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
            return true;
        }

        if (level > crystal.getType().getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, "&cEnchantment level out of bounds! Must be between 1 and " + crystal.getType().getMaxLevel());
            return true;
        }

        player.getInventory().addItem(crystal.getItemStack());
        EnchantCrystalsUtils.sendSender(sender, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal given to player")), crystal.getType(), crystal.getAmount(), crystal.getLevel(), player));
        EnchantCrystalsUtils.sendSender(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal received")), crystal.getType(), crystal.getAmount(), crystal.getLevel(), player));
        return true;
    }
}

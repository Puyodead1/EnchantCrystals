package me.puyodead.enchantcrystals.Commands;

import me.puyodead.enchantcrystals.Crystal;
import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
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
            // player specified one argument, aka a crystal name OR command 'enchants'
//            if (args[0].equals("enchants")) {
//                HashMap<NamespacedKey, CrystalType> crystals = CrystalType.getCrystals();
//                for (final CrystalType c : crystals.values()) {
//                    EnchantCrystalsUtils.sendPlayer(player, c.getKey().getKey());
//                }
//            } else {
//                final String enchantmentName = args[0].toLowerCase();
//                return giveCrystal(player, enchantmentName);
//            }

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

    private void sendGiveSuccessMessage(Player player, Crystal crystal) {
//        EnchantCrystalsUtils.sendPlayer(player,
//                Objects.requireNonNull(EnchantCrystalsUtils.replace(
//                        Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal given")), e, level, player)));
        EnchantCrystalsUtils.sendPlayer(player, "Given " + crystal.getAmount() + " crystal");
    }

    private Crystal makeCrystal(Enchantment enchantment, int amount, int level) {
        if (Objects.isNull(enchantment)) {
            return null;
        }

        return new Crystal().addEnchantment(enchantment, level).setAmount(amount);
    }

    private boolean ensureCrystalNotNull(Player player, Crystal crystal) {
        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid enchantment"));
            return true;
        }

        player.getInventory().addItem(crystal.build().getItemStack());
        sendGiveSuccessMessage(player, crystal);
        return true;
    }

    public boolean giveCrystal(Player player, String enchantmentName) {
        final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
        final Crystal crystal = makeCrystal(enchantment, 1, 1);
        System.out.println(crystal);
        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player player, String enchantmentName, int amount) {
        final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
        final Crystal crystal = makeCrystal(enchantment, amount, 1);
        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player player, String enchantmentName, int amount, int level) {
        final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
        if (Objects.isNull(enchantment)) return false;

        final Crystal crystal = makeCrystal(enchantment, amount, level);

        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid enchantment"));
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, "&cEnchantment level out of bounds! Must be between 1 and " + enchantment.getMaxLevel());
            return true;
        }

        return ensureCrystalNotNull(player, crystal);
    }

    public boolean giveCrystal(Player sender, String enchantmentName, int amount, int level, Player player) {
        final Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentName.toLowerCase()));
        if (Objects.isNull(enchantment)) return false;
        final Crystal crystal = makeCrystal(enchantment, amount, level);

        if (Objects.isNull(crystal)) {
            EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid enchantment"));
            return true;
        }

        if (level > enchantment.getMaxLevel()) {
            EnchantCrystalsUtils.sendPlayer(player, "&cEnchantment level out of bounds! Must be between 1 and " + enchantment.getMaxLevel());
            return true;
        }

        player.getInventory().addItem(crystal.build().getItemStack());
//        EnchantCrystalsUtils.sendSender(sender, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal given to player")), enchantment, crystal.getAmount(), crystal.getLevel(), player));
//        EnchantCrystalsUtils.sendSender(player, EnchantCrystalsUtils.replace(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("messages.crystal received")), crystal.getType(), crystal.getAmount(), crystal.getLevel(), player));
        EnchantCrystalsUtils.sendPlayer(sender, "success");
        EnchantCrystalsUtils.sendPlayer(player, "got crystal");
        return true;
    }
}

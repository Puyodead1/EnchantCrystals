package me.puyodead.enchantcrystals.Commands;

import me.puyodead.enchantcrystals.EnchantCrystals;
import me.puyodead.enchantcrystals.EnchantCrystalsUtils;
import me.puyodead.enchantcrystals.Crystal;
import me.puyodead.enchantcrystals.CrystalType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCrystalCommand implements CommandExecutor {

    private final EnchantCrystals enchantCrystals = EnchantCrystals.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final Player player = sender instanceof Player ? (Player)sender : null;
        // player is not null
        if ((player != null && player.hasPermission("enchantcrystals.givecrystal")) || sender instanceof ConsoleCommandSender) {
            final int l = args.length;
            if (l == 0) {
                // TODO: GUI?
                EnchantCrystalsUtils.sendSender(sender, "&6EnchantCrystals v" + EnchantCrystals.plugin.getDescription().getVersion() + " by Puyodead1");
                EnchantCrystalsUtils.sendSender(sender, "&6=============================");
                EnchantCrystalsUtils.sendSender(sender, "&7/gc <enchant name>");
                EnchantCrystalsUtils.sendSender(sender, "&7/gc <enchant name> <amount>");
                EnchantCrystalsUtils.sendSender(sender, "&7/gc <enchant name> <amount> <level>");
                EnchantCrystalsUtils.sendSender(sender, "&7/gc <enchant name> <amount> <level> <player>");
                EnchantCrystalsUtils.sendSender(sender, "&7If an amount is not specified, one is default");
                EnchantCrystalsUtils.sendSender(sender, "&7If a level is not specified, max is default");
                EnchantCrystalsUtils.sendSender(sender, "&7If a player is not specified, current player is default");
                return false;
            } else if (l == 1) {
                if(player != null) {
                    // player specified one argument, aka a crystal name
                    final String crystalName = args[0].toUpperCase();
                    final CrystalType crystalType = CrystalType.valueOf(crystalName);
                    if (crystalType != null) {
                        // crystal name is valid
                        player.getInventory().addItem(new Crystal(crystalType, crystalType.getCrystalMaxLevel()).getCrystalItem());
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.crystal given to you").replace("%CRYSTAL_AMOUNT%", 1 + "").replace("%CRYSTAL_TYPE%", crystalType.getCrystalName()));
                        return true;
                    } else {
                        // invalid crystal name
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
                        return false;
                    }
                } else {
                    sender.sendMessage(EnchantCrystalsUtils.Color("Only players can use that command!"));
                    return false;
                }
            } else if (l == 2) {
                if (player != null) {
                    //player specified crystal name and amount
                    final String crystalName = args[0].toUpperCase();
                    final int crystalAmount = Integer.parseInt(args[1]);
                    final CrystalType crystalType = CrystalType.valueOf(crystalName);
                    if (crystalType != null) {
                        // crystal name is valid
                        final ItemStack crystalItem = new Crystal(crystalType, crystalType.getCrystalMaxLevel()).getCrystalItem();
                        crystalItem.setAmount(crystalAmount);
                        player.getInventory().addItem(crystalItem);
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.crystal given to you").replace("%CRYSTAL_AMOUNT%", 1 + "").replace("%CRYSTAL_TYPE%", crystalType.getCrystalName()));
                        return true;
                    } else {
                        // invalid crystal name
                        EnchantCrystalsUtils.sendPlayer(player, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
                        return false;
                    }
                } else {
                    sender.sendMessage(EnchantCrystalsUtils.Color("Only players can use that command!"));
                    return false;
                }
            } else if (l == 3) {
                // player specified crystal name, amount, and the level to give it to
                final String crystalName = args[0].toUpperCase();
                final int crystalAmount = Integer.parseInt(args[1]);
                final int crystalLevel = Integer.parseInt(args[2]);

                if (player != null) {
                    final CrystalType crystalType = CrystalType.valueOf(crystalName);
                    if (crystalType != null) {
                        // crystal name is valid
                        final ItemStack crystalItem = new Crystal(crystalType, crystalLevel).getCrystalItem();
                        crystalItem.setAmount(crystalAmount);
                        player.getInventory().addItem(crystalItem);
                        EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.crystal given to you").replace("%CRYSTAL_AMOUNT%", 1 + "").replace("%CRYSTAL_TYPE%", crystalType.getCrystalName()));
                        return true;
                    } else {
                        // invalid crystal name
                        EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
                        return false;
                    }
                } else {
                    EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.invalid player specified"));
                }
                return true;
            } else if (l == 4) {
                // player specified crystal name, amount, the player to give it to, and the crystal level
                final String crystalName = args[0].toUpperCase();
                final int crystalAmount = Integer.parseInt(args[1]);
                final int crystalLevel = Integer.parseInt(args[2]);
                final Player p = Bukkit.getServer().getPlayer(args[3]);

                if (p != null) {
                    final CrystalType crystalType = CrystalType.valueOf(crystalName);
                    if (crystalType != null) {
                        // crystal name is valid
                        final ItemStack crystalItem = new Crystal(crystalType, crystalLevel).getCrystalItem();
                        crystalItem.setAmount(crystalAmount);
                        p.getInventory().addItem(crystalItem);
                        EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.crystal given").replace("%PLAYER_NAME%", p.getName()).replace("%CRYSTAL_AMOUNT%", 1 + "").replace("%CRYSTAL_TYPE%", crystalType.getCrystalName()));
                        return true;
                    } else {
                        // invalid crystal name
                        EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.invalid crystal name"));
                        return false;
                    }
                } else {
                    EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.invalid player specified"));
                    return false;
                }
            }
        } else {
            EnchantCrystalsUtils.sendSender(sender, EnchantCrystals.plugin.getConfig().getString("messages.no permission"));
            return false;
        }
        return false;
    }
}

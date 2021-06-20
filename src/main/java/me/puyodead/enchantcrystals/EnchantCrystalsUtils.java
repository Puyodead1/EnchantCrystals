package me.puyodead.enchantcrystals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantCrystalsUtils {

    public static String Color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(Color(msg));
    }

    public static void sendPlayer(Player player, String msg) {
        player.sendMessage(Color(msg));
    }

    public static void sendSender(CommandSender commandSender, String msg) {
        commandSender.sendMessage(Color(msg));
    }

    public static void sendSenderList(CommandSender commandSender, List<String> list) {
        for (String s : list) {
            commandSender.sendMessage(Color(s));
        }
    }

    public static List<String> ColorList(List<String> list, Enchantment crystalEnchantment, int crystalLevel) {
        Bukkit.broadcastMessage(crystalEnchantment.getKey().toString());
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            newList.add(Color(s.replace("%CRYSTAL_TYPE%", normalize(crystalEnchantment.getName())).replace("%CRYSTAL_LEVEL%", String.valueOf(crystalLevel))));
        }

        return newList;
    }

    public static ItemStack addEnchantToItem(ItemStack is, Enchantment enchant, int level, Boolean allowUnsafe) {
        final Material mat = is != null ? is.getType() : null;
        if (mat != null && !mat.equals(Material.valueOf(EnchantCrystals.plugin.getConfig().getString("settings.crystal.material")))) {
            if (!is.containsEnchantment(enchant)) {
                // add
                if (allowUnsafe) {
                    is.addUnsafeEnchantment(enchant, level);
                } else {
                    is.addEnchantment(enchant, level);
                }
                return is;
            } else {
                // contains
                return null;
            }
        }
        return null;
    }

    public static String normalize(String string) {
        String[] split = string.toLowerCase().split("_");
        ArrayList<String> list = new ArrayList<String>();
        for (String s : split) {
            list.add(s.substring(0, 1).toUpperCase() + s.substring(1));
        }

        return String.join(" ", list);
    }

    public static String replace(String string, CrystalType type, int amount, int level) {
        return string.replace("%CRYSTAL_AMOUNT%", String.valueOf(amount))
                .replace("%CRYSTAL_TYPE%", EnchantCrystalsUtils.normalize(type.getName()))
                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(type.getName()))))
                .replace("%CRYSTAL_LEVEL%", String.valueOf(level));
    }

    public static String replace(String string, CrystalType type, int level, ItemStack item) {
        return string.replace("%ITEM_TYPE%", normalize(item.getType().name()))
                .replace("%CRYSTAL_TYPE%", EnchantCrystalsUtils.normalize(type.getName()))
                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(type.getName()))))
                .replace("%CRYSTAL_LEVEL%", String.valueOf(level));
    }

    public static String replace(String string, CrystalType type, int amount, int level, Player player) {
        return string.replace("%CRYSTAL_AMOUNT%", String.valueOf(amount))
                .replace("%CRYSTAL_TYPE%", EnchantCrystalsUtils.normalize(type.getName()))
                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(type.getName()))))
                .replace("%CRYSTAL_LEVEL%", String.valueOf(level))
                .replace("%PLAYER_NAME%", player.getName());
    }
}

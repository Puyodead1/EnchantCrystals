package me.puyodead.enchantcrystals;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.chat.TranslatableComponent;
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

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendConsole(String msg) {
        Bukkit.getConsoleSender().sendMessage(colorize(msg));
    }

    public static void sendPlayer(Player player, String msg) {
        player.sendMessage(colorize(msg));
    }

    public static void sendSender(CommandSender commandSender, String msg) {
        commandSender.sendMessage(colorize(msg));
    }

    public static void sendSenderList(CommandSender commandSender, List<String> list) {
        for (String s : list) {
            commandSender.sendMessage(colorize(s));
        }
    }

    public static List<String> colorList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            newList.add(colorize(s));
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

    //    public static String replace(String string, String type, int amount, int level) {
//        return string.replace("%CRYSTAL_AMOUNT%", String.valueOf(amount))
//                .replace("%CRYSTAL_TYPE%", type)
//                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", type)))
//                .replace("%CRYSTAL_LEVEL%", translateEnchantmentLevel(level));
//    }
//
//    public static String replace(String string, String type, int amount) {
//        return string.replace("%CRYSTAL_AMOUNT%", String.valueOf(amount))
//                .replace("%CRYSTAL_TYPE%", type)
//                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", type)));
//    }
//
//    public static String replace(String string, Enchantment enchantment, int amount, int level) {
//        return string.replace("%CRYSTAL_AMOUNT%", String.valueOf(amount))
//                .replace("%CRYSTAL_TYPE%", translateEnchantmentName(enchantment))
//                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(enchantment.getName()))))
//                .replace("%CRYSTAL_LEVEL%", translateEnchantmentLevel(level));
//    }
//
    public static String replace(String string, Enchantment enchantment, int level) {
        return string
                .replace("%CRYSTAL_TYPE%", translateEnchantmentName(enchantment))
                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(enchantment.getName()))))
                .replace("%CRYSTAL_LEVEL%", translateEnchantmentLevel(level));
    }

    public static String replace(String string, Enchantment enchantment, int level, ItemStack item) {
        return string.replace("%ITEM_TYPE%", normalize(item.getType().name()))
                .replace("%CRYSTAL_TYPE%", translateEnchantmentName(enchantment))
                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", normalize(enchantment.getName()))))
                .replace("%CRYSTAL_LEVEL%", translateEnchantmentLevel(level))
                .replace("%ITEM_DISPLAY_NAME%", item.getItemMeta() != null && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : normalize(item.getType().name()));
    }

    public static String replace(String string, int amountOfEnchants) {
        return string.replace("%ENCHANTMENTS_SIZE%", String.valueOf(amountOfEnchants))
                .replace("%ENCHANT_STRING%", Objects.requireNonNull(amountOfEnchants == 1 ? EnchantCrystals.plugin.getConfig().getString("messages.enchant") : EnchantCrystals.plugin.getConfig().getString("messages.enchants")));
    }

//    public static String replace(String string, Enchantment enchantment, int level, Player player) {
//        return string
//                .replace("%CRYSTAL_TYPE%", translateEnchantmentName(enchantment))
//                .replace("%DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.crystal.display name")).replace("%CRYSTAL_TYPE%", translateEnchantmentName(enchantment))))
//                .replace("%CRYSTAL_LEVEL%", translateEnchantmentLevel(level))
//                .replace("%PLAYER_NAME%", player.getName());
//    }

    public static String translateEnchantmentName(Enchantment e) {
        return new TranslatableComponent("enchantment.minecraft." + e.getKey().getKey()).toPlainText();
    }

    public static String translateEnchantmentLevel(int level) {
        return new TranslatableComponent("enchantment.level." + level).toPlainText();
    }

    public static boolean isCrystal(NBTItem nbtItem) {
        return nbtItem.hasKey("enchantcrystals:isEnchantCrystal");
    }
}

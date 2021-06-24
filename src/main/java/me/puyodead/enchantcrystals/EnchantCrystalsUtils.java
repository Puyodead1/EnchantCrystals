package me.puyodead.enchantcrystals;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantCrystalsUtils {

    public static String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendConsole(final String msg) {
        Bukkit.getConsoleSender().sendMessage(colorize(msg));
    }

    public static void sendPlayer(final Player player, final String msg) {
        player.sendMessage(colorize(msg));
    }

    public static void sendSender(final CommandSender commandSender, final String msg) {
        commandSender.sendMessage(colorize(msg));
    }

    public static void sendSenderList(final CommandSender commandSender, final List<String> list) {
        for (String s : list) {
            commandSender.sendMessage(colorize(s));
        }
    }

    public static List<String> colorList(final List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String s : list) {
            newList.add(colorize(s));
        }

        return newList;
    }

    public static String normalize(final String string) {
        String[] split = string.toLowerCase().split("_");
        ArrayList<String> list = new ArrayList<String>();
        for (String s : split) {
            list.add(s.substring(0, 1).toUpperCase() + s.substring(1));
        }

        return String.join(" ", list);
    }

    public static String replace(final String string, final Enchantment enchantment, final int level, final ItemStack itemStack) {
        return string.replace("%ITEM_TYPE%", normalize(itemStack.getType().name()))
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment))
                .replace("%CRYSTAL_DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.item.display_name")).replace("%ENCHANTMENT_NAME%", normalize(enchantment.getName()))))
                .replace("%ENCHANTMENT_LEVEL%", translateEnchantmentLevel(level))
                .replace("%ITEM_DISPLAY_NAME%", itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : normalize(itemStack.getType().name()));
    }

    public static String replaceDisplayName(final String string, final int amountOfEnchants) {
        return string.replace("%ENCHANTMENT_COUNT%", String.valueOf(amountOfEnchants))
                .replace("%ENCHANTMENT_STRING%", Objects.requireNonNull(amountOfEnchants == 1 ? EnchantCrystals.plugin.getConfig().getString("messages.enchantment_singular") : EnchantCrystals.plugin.getConfig().getString("messages.enchantment_plural")));
    }

    public static String replaceInvalid(final String string, final Enchantment enchantment, final ItemStack itemStack) {
        return string.replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment))
                .replace("%ITEM_DISPLAY_NAME%", itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : normalize(itemStack.getType().name()));
    }

    public static String replaceConflicting(final String string, final Enchantment e1, final Enchantment e2) {
        return string.replace("%ENCHANTMENT_NAME%", translateEnchantmentName(e1))
                .replace("%OTHER_ENCHANTMENT_NAME%", translateEnchantmentName(e2));
    }

    public static String replaceUpgraded(final String string, final Enchantment enchantment, final int oldLevel, final int newLevel) {
        return string.replace("%ENCHANTMENT_LEVEL_OLD%", String.valueOf(oldLevel))
                .replace("%ENCHANTMENT_LEVEL_NEW%", String.valueOf(newLevel))
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment));
    }

    public static String replaceSuccess(final String string, final Enchantment enchantment, final int level, final int amount) {
        return string
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment) + " " + translateEnchantmentLevel(level))
                .replace("%CRYSTAL_AMOUNT%", String.valueOf(amount));
    }

    public static String replaceAdd(String string, Enchantment enchantment, int level) {
        return string
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment) + " " + translateEnchantmentLevel(level));
    }

    public static String replaceExceed(String string, Enchantment enchantment) {
        return string
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment));
    }

    public static String replace(final String string, final Enchantment enchantment, final int level, final int amount, final Player player) {
        return string
                .replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment))
                .replace("%CRYSTAL_DISPLAY_NAME%", Objects.requireNonNull(Objects.requireNonNull(EnchantCrystals.plugin.getConfig().getString("settings.item.display_name")).replace("%ENCHANTMENT_NAME%", translateEnchantmentName(enchantment))))
                .replace("%ENCHANTMENT_LEVEL%", translateEnchantmentLevel(level))
                .replace("%PLAYER_NAME%", player.getName())
                .replace("%CRYSTAL_AMOUNT%", String.valueOf(amount));
    }

    public static String translateEnchantmentName(final Enchantment e) {
        return new TranslatableComponent("enchantment.minecraft." + e.getKey().getKey()).toPlainText();
    }

    public static String translateEnchantmentLevel(final int level) {
        return new TranslatableComponent("enchantment.level." + level).toPlainText();
    }

    public static boolean isCrystal(final NBTItem nbtItem) {
        // TODO: Since we add custom NBT to tell if an item is a crystal, we could convert crystal materials if they are changed in the config
        return nbtItem.hasKey("enchantcrystals:isEnchantCrystal");
    }

}

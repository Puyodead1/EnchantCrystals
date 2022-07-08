package me.puyodead1.enchantcrystals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utils {
    public static String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void sendConsole(final String msg) {
        Bukkit.getConsoleSender().sendMessage(colorize(msg));
    }

    public static String replaceDisplayName(final String string, final int enchantCount) {
        return string.replace("%ENCHANTMENT_COUNT%", String.valueOf(enchantCount))
                .replace("%ENCHANTMENT_STRING%", enchantCount == 1 ? EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_singular") : EnchantCrystals.getPlugin().getConfig().getString("messages.enchantment_plural"));
    }
}

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

    public static List<String> ColorList(List<String> list) {
        List<String> newList = new ArrayList<>();
        for(String s : list) {
            newList.add(Color(s));
        }

        return newList;
    }

    public static List<String> ColorList(List<String> list, Enchantment crystalEnchantment, int crystalLevel) {
        List<String> newList = new ArrayList<>();
        for(String s : list) {
            newList.add(Color(s.replace("%CRYSTAL_TYPE%", crystalEnchantment.getName()).replace("%CRYSTAL_LEVEL%", crystalLevel + "")));
        }

        return newList;
    }

    public static ItemStack addEnchantToItem(ItemStack is, Enchantment enchant, int level) {
        final Material mat = is != null ? is.getType() : null;
        if(mat != null && !mat.equals(Material.NETHER_STAR)) {
            if(!is.containsEnchantment(enchant)) {
                // add
                is.addEnchantment(enchant, level);
                return is;
            } else {
                // contains
                return null;
            }
        }
        return null;
    }
}

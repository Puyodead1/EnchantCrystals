package me.puyodead.enchantcrystals.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.InvocationTargetException;

public interface NMSBase {
    void onEnchantmentPerformed(Player player, int cost, InventoryView view) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException;
}

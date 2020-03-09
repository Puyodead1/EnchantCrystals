package me.puyodead.enchantcrystals;

import me.puyodead.enchantcrystals.Commands.GiveCrystalCommand;
import me.puyodead.enchantcrystals.Events.CrystalUseEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantCrystals extends JavaPlugin {

    public static EnchantCrystals plugin;
    private static final String PREFIX = "&7[&dEnchantCrystals&7] ";

    @Override
    public void onEnable() {
        plugin = this;

        EnchantCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");

        initConfig();
        initEvents();
        initCommands();
        initCrystals();

        EnchantCrystalsUtils.sendConsole(PREFIX + "&d========================");
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bAuthor: &ePuyodead1");
        EnchantCrystalsUtils.sendConsole(PREFIX + "&b" + this.getName() + " Version: &e" + getServer().getPluginManager().getPlugin(this.getDescription().getName()).getDescription().getVersion());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bMinecraft Version: &e" + getServer().getVersion());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");
    }

    @Override
    public void onDisable() {
        CrystalType.getCrystals().clear();
    }

    public void initConfig() {
        final long STARTED = System.currentTimeMillis();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Configuration &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initEvents() {
        final long STARTED = System.currentTimeMillis();

       getServer().getPluginManager().registerEvents(new CrystalUseEvent(), this);

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Events &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCommands() {
        final long STARTED = System.currentTimeMillis();

        getCommand("givecrystal").setExecutor(new GiveCrystalCommand());

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCrystals() {
        final long STARTED = System.currentTimeMillis();
        for(Enchantment e : EnchantmentWrapper.values()) {
            new CrystalType(e, getConfig().getStringList("settings.crystal lore"));
        }

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }
}

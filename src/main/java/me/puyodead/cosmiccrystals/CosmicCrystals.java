package me.puyodead.cosmiccrystals;

import me.puyodead.cosmiccrystals.Commands.GiveCrystalCommand;
import me.puyodead.cosmiccrystals.Events.CrystalUseEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class CosmicCrystals extends JavaPlugin {

    public static CosmicCrystals plugin;
    private static final String PREFIX = "&7[&dCosmicCrystals&7] ";

    @Override
    public void onEnable() {
        plugin = this;

        CosmicCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");

        initConfig();
        initEvents();
        initCommands();
        initCrystals();

        CosmicCrystalsUtils.sendConsole(PREFIX + "&d========================");
        CosmicCrystalsUtils.sendConsole(PREFIX + "&bAuthor: &ePuyodead1");
        CosmicCrystalsUtils.sendConsole(PREFIX + "&b" + this.getName() + " Version: &e" + getServer().getPluginManager().getPlugin(this.getDescription().getName()).getDescription().getVersion());
        CosmicCrystalsUtils.sendConsole(PREFIX + "&bMinecraft Version: &e" + getServer().getVersion());
        CosmicCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void initConfig() {
        final long STARTED = System.currentTimeMillis();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        CosmicCrystalsUtils.sendConsole(PREFIX + "&bLoaded Configuration &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initEvents() {
        final long STARTED = System.currentTimeMillis();

       getServer().getPluginManager().registerEvents(new CrystalUseEvent(), this);

        CosmicCrystalsUtils.sendConsole(PREFIX + "&bLoaded Events &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCommands() {
        final long STARTED = System.currentTimeMillis();

        getCommand("givecrystal").setExecutor(new GiveCrystalCommand());

        CosmicCrystalsUtils.sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCrystals() {
        final long STARTED = System.currentTimeMillis();
        for(Enchantment e : EnchantmentWrapper.values()) {
            new CrystalType(e, getConfig().getStringList("settings.crystal lore"));
        }

        CosmicCrystalsUtils.sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }
}

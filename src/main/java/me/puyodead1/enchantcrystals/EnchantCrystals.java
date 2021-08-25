package me.puyodead1.enchantcrystals;

import me.puyodead1.enchantcrystals.commands.EnchantCrystalsCommand;
import me.puyodead1.enchantcrystals.commands.TabCompletion;
import me.puyodead1.enchantcrystals.events.AnvilPrepareEvent;
import me.puyodead1.enchantcrystals.events.CrystalUseEvent;
import me.puyodead1.enchantcrystals.events.ItemEnchantEvent;
import me.puyodead1.enchantcrystals.events.OpenInventoryEvent;
import me.puyodead1.enchantcrystals.nms.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EnchantCrystals extends JavaPlugin {

    private static EnchantCrystals plugin;
    private static final String PREFIX = "&7[&dEnchantCrystals&7] ";
    private NMSBase nms;

    @Override
    public void onEnable() {
        plugin = this;

        EnchantCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");
        if (Version.isOlder(Version.v1_8_R3)) {
            EnchantCrystalsUtils.sendConsole(PREFIX + "&cThis server is running 1.8.3 or older which is not supported, plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        switch (Version.getCurrentVersion()) {
            case v1_13_R2:
                nms = new NMS_v1_13_R2();
                break;
            case v1_14_R1:
                nms = new NMS_v1_14_R1();
                break;
            case v1_15_R1:
                nms = new NMS_v1_15_R1();
                break;
            case v1_16_R1:
            case v1_16_R2:
            case v1_16_R3:
                nms = new NMS_v1_16();
                break;
            case v1_17_R1:
                nms = new NMS_v1_17_R1();
                break;
        }

        initConfig();
        initEvents();
        initCommands();

        EnchantCrystalsUtils.sendConsole(PREFIX + "&d========================");
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bAuthor: &ePuyodead1");
        EnchantCrystalsUtils.sendConsole(PREFIX + "&b" + this.getName() + " Version: &e" + Objects.requireNonNull(getServer().getPluginManager().getPlugin(this.getDescription().getName())).getDescription().getVersion());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bMinecraft Version: &e" + getServer().getVersion());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bBukkit Version: &e" + getServer().getBukkitVersion());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&bNMS Version: &e" + Version.getCurrentVersion().getVersionInteger());
        EnchantCrystalsUtils.sendConsole(PREFIX + "&b=============================================================");
    }

    @Override
    public void onDisable() {
        plugin = null;
        nms = null;
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
        getServer().getPluginManager().registerEvents(new ItemEnchantEvent(this), this);
        getServer().getPluginManager().registerEvents(new OpenInventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new AnvilPrepareEvent(), this);

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Events &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCommands() {
        final long STARTED = System.currentTimeMillis();
        final PluginCommand enchantcrystals = getCommand("enchantcrystals");

        enchantcrystals.setExecutor(new EnchantCrystalsCommand());
        enchantcrystals.setTabCompleter(new TabCompletion());

        EnchantCrystalsUtils.sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public static EnchantCrystals getPlugin() {
        return plugin;
    }

    public NMSBase getNMS() {
        return nms;
    }
}

package me.puyodead1.enchantcrystals;

import me.puyodead1.enchantcrystals.commands.EnchantCrystalsCommand;
import me.puyodead1.enchantcrystals.commands.TabCompleter;
import me.puyodead1.enchantcrystals.hooks.Enchantment;
import me.puyodead1.enchantcrystals.hooks.PluginHook;
import me.puyodead1.enchantcrystals.hooks.pluginhooks.EPluginHooks;
import me.puyodead1.enchantcrystals.hooks.pluginhooks.advancedenchantments.AEHook;
import me.puyodead1.enchantcrystals.hooks.pluginhooks.enchantmentsolution.ESHook;
import me.puyodead1.enchantcrystals.hooks.pluginhooks.mojang.MojangHook;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.puyodead1.enchantcrystals.Utils.sendConsole;

public final class EnchantCrystals extends JavaPlugin {
    private static EnchantCrystals plugin;
    public static final String PREFIX = "&7[&dEnchantCrystals&7] ";

    public static final HashMap<String, PluginHook> hooks = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        sendConsole(PREFIX + "&d=============================================================");
        sendConsole(PREFIX + "&bAuthor: &ePuyodead1");
        sendConsole(PREFIX + String.format("&b%s Version: &e%s", this.getName(), this.getDescription().getVersion()));
        sendConsole(PREFIX + String.format("&bMinecraft Version: &e%s", this.getServer().getVersion()));
        sendConsole(PREFIX + String.format("&bBukkit Version: &e%s", this.getServer().getBukkitVersion()));
//        EnchantCrystalsUtils.sendConsole(PREFIX + "&bNMS Version: &e" + Version.getNMSVersionString() + " (" + Version.getNMSVersionInt() + ")");
        sendConsole(PREFIX + "&d=========================");

        initConfig();
        initEvents();
        initCommands();
        sendConsole(PREFIX + "&d=========================");
        loadHooks();

        sendConsole(PREFIX + "&d=============================================================");
    }

    @Override
    public void onDisable() {
        // loop the loaded hooks and clear the enchantments
        for (final PluginHook hook : EnchantCrystals.hooks.values()) {
            getLogger().info(String.format("Unloading hook: %s", hook.getName()));
            hook.getEnchantments().clear();
        }
        EnchantCrystals.hooks.clear();
        plugin = null;
    }

    public void initConfig() {
        final long STARTED = System.currentTimeMillis();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        sendConsole(PREFIX + String.format("&bLoaded Configuration &e(took %sms)", (System.currentTimeMillis() - STARTED)));
    }

    public void initEvents() {
        final long STARTED = System.currentTimeMillis();

//        getServer().getPluginManager().registerEvents(new CrystalUseEvent(), this);
//        getServer().getPluginManager().registerEvents(new ItemEnchantEvent(this), this);
//        getServer().getPluginManager().registerEvents(new OpenInventoryEvent(), this);
//        getServer().getPluginManager().registerEvents(new AnvilPrepareEvent(), this);

        sendConsole(PREFIX + String.format("&bLoaded Events &e(took %sms)", (System.currentTimeMillis() - STARTED)));
    }

    public void initCommands() {
        final long STARTED = System.currentTimeMillis();
        final PluginCommand enchantcrystals = getCommand("enchantcrystals");
//
        enchantcrystals.setExecutor(new EnchantCrystalsCommand());
        enchantcrystals.setTabCompleter(new TabCompleter());

        sendConsole(PREFIX + String.format("&bLoaded Commands &e(took %sms)", (System.currentTimeMillis() - STARTED)));
    }

    /**
     * Loads all hooks
     */
    public void loadHooks() {
        final long STARTED = System.currentTimeMillis();

        // Vanilla enchants should always be loaded
        loadHook(MojangHook.class);

        // Optional plugin hooks
        tryLoadHook(EPluginHooks.ADVANCED_ENCHANTMENTS.toString(), AEHook.class);
        tryLoadHook(EPluginHooks.ENCHANTMENT_SOLUTION.toString(), ESHook.class);

        sendConsole(PREFIX + String.format("&bLoaded &e%s &bHooks &e(took %s ms)", EnchantCrystals.hooks.size(), (System.currentTimeMillis() - STARTED)));
    }

    /**
     * Checks if a plugin is loaded before trying to load the hook
     *
     * @param pluginName the plugin to check for
     * @param clazz      the class to load
     */
    public <T extends PluginHook> void tryLoadHook(final String pluginName, final Class<T> clazz) {
        if (!pluginName.isEmpty() && !Objects.isNull(Bukkit.getPluginManager().getPlugin(pluginName))) {
            sendConsole(PREFIX + String.format("&bHooking plugin &e%s", pluginName));
            loadHook(clazz);
        }
    }

    /**
     * Loads a hook class
     *
     * @param clazz the class to load
     */
    public <T extends PluginHook> void loadHook(final Class<T> clazz) {
        try {
            final T hook = clazz.getDeclaredConstructor().newInstance();
            sendConsole(PREFIX + String.format("&bLoading Hook: &e%s", hook.getName()));
            hook.loadEnchantments();
            sendConsole(PREFIX + String.format("&d[&b%s&d]: &eLoaded %s enchantments&b", hook.getName(), hook.getEnchantments().size()));

            hooks.put(hook.getName(), hook);
        } catch (final Exception ex) {
            getLogger().severe(ex.getMessage());
        }
    }

    /**
     * Finds plugin hook with registered enchantment name
     *
     * @param key name of the enchantment
     * @return PluginHook that registered the enchantment
     */
    public static PluginHook getHookForEnchantment(String key) {
        return EnchantCrystals.hooks.values().stream().filter(h -> h.hasEnchantment(key)).findFirst().get();
    }

    /**
     * Gets an enchantment by its key
     *
     * @param key enchantment key
     * @return Enchantment
     */
    public static Enchantment getEnchantment(String key) {
        final PluginHook pluginHook = EnchantCrystals.getHookForEnchantment(key);
        return pluginHook.getEnchantment(key);
    }

    public static List<String> getEnchantmentKeys() {
        List<String> enchantments = new ArrayList<>();
        EnchantCrystals.hooks.values().forEach(h -> h.getEnchantments().forEach(e -> enchantments.add(e.getKey())));
        return enchantments;
    }

    public static EnchantCrystals getPlugin() {
        return plugin;
    }
}

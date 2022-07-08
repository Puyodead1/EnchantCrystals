package me.puyodead1.enchantcrystals;

import me.puyodead1.enchantcrystals.commands.TestCommand;
import me.puyodead1.enchantcrystals.hooks.Enchantment;
import me.puyodead1.enchantcrystals.hooks.PluginHook;
import me.puyodead1.enchantcrystals.hooks.pluginhooks.AEHook;
import me.puyodead1.enchantcrystals.hooks.IEnchantment;
import me.puyodead1.enchantcrystals.hooks.IPluginHook;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static me.puyodead1.enchantcrystals.Utils.sendConsole;

public final class EnchantCrystals extends JavaPlugin {
    private static EnchantCrystals plugin;
    public static final String PREFIX = "&7[&dEnchantCrystals&7] ";

    public static final HashMap<String, PluginHook> hooks = new HashMap<>();
    public static final List<IEnchantment> enchantments = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;

        sendConsole(PREFIX + "&d=============================================================");
        sendConsole(PREFIX + "&bAuthor: &ePuyodead1");
        sendConsole(PREFIX + "&b" + this.getName() + " Version: &e" + Objects.requireNonNull(getServer().getPluginManager().getPlugin(this.getDescription().getName())).getDescription().getVersion());
        sendConsole(PREFIX + "&bMinecraft Version: &e" + getServer().getVersion());
        sendConsole(PREFIX + "&bBukkit Version: &e" + getServer().getBukkitVersion());
//        EnchantCrystalsUtils.sendConsole(PREFIX + "&bNMS Version: &e" + Version.getNMSVersionString() + " (" + Version.getNMSVersionInt() + ")");
        sendConsole(PREFIX + "&d=========================");

        initConfig();
        initEvents();
        initCommands();
        loadHooks();

        sendConsole(PREFIX + "&d=============================================================");
    }

    @Override
    public void onDisable() {
        plugin = null;
    }

    public void initConfig() {
        final long STARTED = System.currentTimeMillis();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        sendConsole(PREFIX + "&bLoaded Configuration &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initEvents() {
        final long STARTED = System.currentTimeMillis();

//        getServer().getPluginManager().registerEvents(new CrystalUseEvent(), this);
//        getServer().getPluginManager().registerEvents(new ItemEnchantEvent(this), this);
//        getServer().getPluginManager().registerEvents(new OpenInventoryEvent(), this);
//        getServer().getPluginManager().registerEvents(new AnvilPrepareEvent(), this);

        sendConsole(PREFIX + "&bLoaded Events &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void initCommands() {
        final long STARTED = System.currentTimeMillis();
//        final PluginCommand enchantcrystals = getCommand("enchantcrystals");
        final PluginCommand test = getCommand("test");
        test.setExecutor(new TestCommand());
//
//        enchantcrystals.setExecutor(new EnchantCrystalsCommand());
//        enchantcrystals.setTabCompleter(new TabCompletion());

        sendConsole(PREFIX + "&bLoaded Commands &e(took " + (System.currentTimeMillis() - STARTED) + "ms)");
    }

    public void loadHooks() {
        sendConsole(PREFIX + "&bLoading Hook: AdvancedEnchantments");
        AEHook advancedEnchantmentsHook = new AEHook();
        advancedEnchantmentsHook.loadEnchantments();
        sendConsole(PREFIX + String.format("&b[Hook: &eAdvancedEnchantments&b]: &eLoaded %s enchantments.", advancedEnchantmentsHook.getEnchantments().size()));

        hooks.put(advancedEnchantmentsHook.getName(), advancedEnchantmentsHook);
    }

    /**
     * Finds plugin hook with registered enchantment name
     * @param enchantmentName name of the enchantment
     * @return PluginHook that registered the enchantment
     */
    public static PluginHook getHookForEnchantment(String enchantmentName) {
        return EnchantCrystals.hooks.values().stream().filter(h -> h.hasEnchantment(enchantmentName)).findFirst().orElseThrow(NoSuchElementException::new);
    }

    /**
     * Gets an enchantment by its display name or key
     * @param enchantmentName Enchant display name or key to get
     * @return Enchantment
     */
    public static Enchantment getEnchantmentByName(String enchantmentName) {
        final PluginHook pluginHook = EnchantCrystals.getHookForEnchantment(enchantmentName);
        return pluginHook.getEnchantmentByName(enchantmentName);
    }

    public static EnchantCrystals getPlugin() {
        return plugin;
    }
}

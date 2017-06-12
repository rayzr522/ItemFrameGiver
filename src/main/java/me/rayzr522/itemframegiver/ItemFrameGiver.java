package me.rayzr522.itemframegiver;

import me.rayzr522.itemframegiver.command.CommandHandler;
import me.rayzr522.itemframegiver.data.CooldownManager;
import me.rayzr522.itemframegiver.data.FrameManager;
import me.rayzr522.itemframegiver.data.GiverFrame;
import me.rayzr522.itemframegiver.event.ItemFrameListener;
import me.rayzr522.itemframegiver.utils.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * @author Rayzr
 */
public class ItemFrameGiver extends JavaPlugin {
    private static ItemFrameGiver instance;
    private MessageHandler messages = new MessageHandler();
    private FrameManager frameManager = new FrameManager();
    private CooldownManager cooldownManager = new CooldownManager();

    public static ItemFrameGiver getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("itemframegiver").setExecutor(new CommandHandler(this));
        getServer().getPluginManager().registerEvents(new ItemFrameListener(this), this);

        reload();
    }

    @Override
    public void onDisable() {
        save();

        instance = null;
    }

    /**
     * (Re)loads all configs from the disk
     */
    public void reload() {
        frameManager.load(getConfig("frames.yml"));
        messages.load(getConfig("messages.yml"));
    }

    public void save() {
        YamlConfiguration frames = new YamlConfiguration();
        frameManager.save(frames);
        saveConfig(frames, "frames.yml");
    }

    /**
     * If the file is not found and there is a default file in the JAR, it saves the default file to the plugin data folder first
     *
     * @param path The path to the config file (relative to the plugin data folder)
     * @return The {@link YamlConfiguration}
     */
    public YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, true);
        }

        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    /**
     * Attempts to save a {@link YamlConfiguration} to the disk, and any {@link IOException}s are printed to the console
     *
     * @param config The config to save
     * @param path   The path to save the config file to (relative to the plugin data folder)
     */
    public void saveConfig(YamlConfiguration config, String path) {
        try {
            config.save(getFile(path));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save config", e);
        }
    }

    /**
     * @param path The path of the file (relative to the plugin data folder)
     * @return The {@link File}
     */
    public File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.separatorChar));
    }

    /**
     * Returns a message from the language file
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }

    /**
     * Returns a message from the language file without adding the prefix
     *
     * @param key     The key of the message to translate
     * @param objects The formatting objects to use
     * @return The formatted message
     */
    public String trRaw(String key, Object... objects) {
        return messages.trRaw(key, objects);
    }

    /**
     * Checks a target {@link CommandSender} for a given permission (excluding the permission base). Example:
     * <p>
     * <pre>
     *     checkPermission(sender, "command.use", true);
     * </pre>
     * <p>
     * This would check if the player had the permission <code>"{plugin name}.command.use"</code>, and if they didn't, it would send them the no-permission message from the messages config file.
     *
     * @param target      The target {@link CommandSender} to check
     * @param permission  The permission to check, excluding the permission base (which is the plugin name)
     * @param sendMessage Whether or not to send a no-permission message to the target
     * @return Whether or not the target has the given permission
     */
    public boolean checkPermission(CommandSender target, String permission, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);

        if (!target.hasPermission(fullPermission)) {
            if (sendMessage) {
                target.sendMessage(tr("no-permission", fullPermission));
            }

            return false;
        }

        return true;
    }

    /**
     * @return The {@link MessageHandler} instance for this plugin
     */
    public MessageHandler getMessages() {
        return messages;
    }

    public FrameManager getFrameManager() {
        return frameManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}

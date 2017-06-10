package me.rayzr522.itemframegiver.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Hashtable;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Rayzr
 */
public class MessageHandler {
    private Hashtable<String, String> messages = new Hashtable<>();

    private static String getBaseKey(String key) {
        return key.substring(0, key.lastIndexOf('.'));
    }

    public void load(ConfigurationSection config) {
        messages.clear();
        config.getKeys(true).forEach(key -> {
            String value;
            if (config.isList(key)) {
                value = config.getList(key).stream().map(Objects::toString).collect(Collectors.joining("\n"));
            } else {
                value = config.get(key).toString();
            }
            messages.put(key, value);
        });
    }

    private String getPrefixFor(String key) {
        String baseKey = getBaseKey(key);
        String basePrefix = messages.getOrDefault(baseKey + ".prefix", messages.getOrDefault("prefix", ""));
        String addon = messages.getOrDefault(baseKey + ".prefix-addon", "");

        return ChatColor.translateAlternateColorCodes('&', basePrefix + addon);
    }

    public String trRaw(String key, Object... objects) {
        return ChatColor.translateAlternateColorCodes('&', String.format(messages.getOrDefault(key, key).replace("\n", "\n" + ChatColor.RESET), objects));
    }

    public String tr(String key, Object... objects) {
        String prefix = getPrefixFor(key);
        return prefix + trRaw(key, objects).replace("\n" + ChatColor.RESET, "\n" + ChatColor.RESET + prefix);
    }
}

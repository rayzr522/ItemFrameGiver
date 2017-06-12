package me.rayzr522.itemframegiver.data;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class GiverFrame {
    private UUID uuid;
    private double cooldown;

    public GiverFrame(UUID uuid, double cooldown) {
        this.uuid = uuid;
        this.cooldown = cooldown;
    }

    public GiverFrame(UUID uuid) {
        this(uuid, 0.0);
    }

    public static GiverFrame deserialize(ConfigurationSection config) {
        UUID uuid = UUID.fromString(config.getString("uuid"));
        double cooldown = config.getDouble("cooldown");

        return new GiverFrame(uuid, cooldown);
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("uuid", uuid.toString())
                .put("cooldown", cooldown)
                .build();
    }

    @Override
    public String toString() {
        return "GiverFrame{" +
                "uuid=" + uuid +
                ", cooldown=" + cooldown +
                '}';
    }
}

package me.rayzr522.itemframegiver.data;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class GiverFrame implements ConfigurationSerializable {
    private UUID uuid;
    private double cooldown;

    public GiverFrame(UUID uuid, double cooldown) {
        this.uuid = uuid;
        this.cooldown = cooldown;
    }

    public GiverFrame(UUID uuid) {
        this(uuid, 0.0);
    }

    @SuppressWarnings("unused")
    private GiverFrame(Map<String, Object> data) {
        this(UUID.fromString(data.get("uuid").toString()), (double) data.get("cooldown"));
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

    @Override
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

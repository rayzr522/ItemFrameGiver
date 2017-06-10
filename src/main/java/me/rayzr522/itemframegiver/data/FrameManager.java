package me.rayzr522.itemframegiver.data;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class FrameManager {
    private Map<UUID, GiverFrame> frameMap = new HashMap<>();

    public Map<UUID, GiverFrame> getFrameMap() {
        return frameMap;
    }

    public GiverFrame getFrame(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null!");

        return frameMap.get(uuid);
    }

    public void addFrame(GiverFrame frame) {
        Objects.requireNonNull(frame, "frame cannot be null!");

        UUID id = frame.getUniqueId();

        if (frameMap.containsKey(id)) {
            throw new IllegalStateException("A GiverFrame with the ID " + id + " was already present!");
        }

        frameMap.put(id, frame);
    }

    public boolean removeFrame(UUID uniqueId) {
        return frameMap.remove(uniqueId) != null;
    }

    public void load(ConfigurationSection config) {
        Objects.requireNonNull(config, "config cannot be null!");

        frameMap.clear();

        System.out.println("Keys: ");
        System.out.println(config.getKeys(false).stream().collect(Collectors.joining(", ")));

        config.getKeys(false).forEach(key -> {
            System.out.println("Loading " + key);
            GiverFrame frame = (GiverFrame) config.get(key);
            frameMap.put(frame.getUniqueId(), frame);
        });
    }

    public void save(ConfigurationSection config) {
        Objects.requireNonNull(config, "config cannot be null!");

        frameMap.forEach((id, frame) -> {
            config.set(id.toString(), frame);
        });
    }
}

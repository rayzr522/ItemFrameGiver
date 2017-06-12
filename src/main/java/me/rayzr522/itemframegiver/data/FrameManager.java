package me.rayzr522.itemframegiver.data;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ItemFrame;

import java.util.*;
import java.util.logging.Logger;
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

        config.getKeys(false).stream().filter(config::isConfigurationSection).forEach(key -> {
            GiverFrame frame = GiverFrame.deserialize(config.getConfigurationSection(key));
            frameMap.put(frame.getUniqueId(), frame);
        });
    }

    public void save(ConfigurationSection config) {
        Objects.requireNonNull(config, "config cannot be null!");

        frameMap.forEach((id, frame) -> {
            config.createSection(id.toString(), frame.serialize());
        });
    }

    public void debug(Logger logger) {
        this.frameMap.values().stream().map(Objects::toString).forEach(logger::info);
    }

    public int clean() {
        return new Object() {
            private ItemFrame getItemFrame(UUID id) {
                for (World world : Bukkit.getWorlds()) {
                    Optional<ItemFrame> itemFrame = world.getEntitiesByClass(ItemFrame.class).stream()
                            .filter(entity -> entity.getUniqueId().equals(id))
                            .findFirst();

                    if (itemFrame.isPresent()) {
                        return itemFrame.get();
                    }
                }

                return null;
            }

            private int clean() {
                int counter = 0;

                for (GiverFrame giverFrame : getFrameMap().values()) {
                    if (getItemFrame(giverFrame.getUniqueId()) == null) {
                        removeFrame(giverFrame.getUniqueId());
                        counter++;
                    }
                }

                return counter;
            }
        }.clean();
    }
}

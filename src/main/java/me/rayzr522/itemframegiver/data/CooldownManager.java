package me.rayzr522.itemframegiver.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class CooldownManager {
    private Map<UUID, PlayerCooldowns> playerCooldownsMap = new HashMap<>();

    /**
     * @param id The {@link UUID} of the {@link org.bukkit.entity.Player} to get the cooldowns for.
     * @return The {@link PlayerCooldowns} for the player.
     */
    public PlayerCooldowns getCooldowns(UUID id) {
        PlayerCooldowns cooldowns = playerCooldownsMap.get(id);

        if (cooldowns == null) {
            cooldowns = new PlayerCooldowns();
            playerCooldownsMap.put(id, cooldowns);
        }

        return cooldowns;
    }

    /**
     * Represents a map of GiverFrame ID -> Last click time.
     */
    public class PlayerCooldowns {
        private Map<UUID, Long> lastInteractions = new HashMap<>();

        public long getLastInteraction(UUID id) {
            return lastInteractions.getOrDefault(id, 0L);
        }

        public void setLastInteraction(UUID id, long time) {
            lastInteractions.put(id, time);
        }
    }
}

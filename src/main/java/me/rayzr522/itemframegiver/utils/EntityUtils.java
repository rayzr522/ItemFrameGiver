package me.rayzr522.itemframegiver.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class EntityUtils {

    public static Entity getTargetEntity(Player player, int range, EntityType type) {
        BlockIterator iterator = new BlockIterator(player, range);

        World world = player.getWorld();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Location location = block.getLocation();

            if (block.getType().isSolid()) {
                // They hit a solid block, there can't be any more entities visible behind this
                return null;
            }

            Collection<Entity> entities = world.getNearbyEntities(location, 1.0, 1.0, 1.0);
            Optional<Entity> entity = entities.stream().filter(e -> e.getType() == type)
                    .sorted((a, b) ->
                            (int) Math.round(b.getLocation().distanceSquared(location) - a.getLocation().distanceSquared(location)))
                    .findFirst();

            if (entity.isPresent()) {
                return entity.get();
            }
        }

        return null;
    }

    public static BlockFace getHorizontalFaceFromLocation(Location location) {
        int direction = (int) (((location.getYaw() + 360 + 45) % 360) / 90);

        switch (direction) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
            default:
                return BlockFace.SELF;
        }
    }
}

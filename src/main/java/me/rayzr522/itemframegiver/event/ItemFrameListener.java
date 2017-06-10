package me.rayzr522.itemframegiver.event;

import me.rayzr522.itemframegiver.ItemFrameGiver;
import me.rayzr522.itemframegiver.data.CooldownManager.PlayerCooldowns;
import me.rayzr522.itemframegiver.data.GiverFrame;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.Map;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class ItemFrameListener implements Listener {
    private ItemFrameGiver plugin;

    public ItemFrameListener(ItemFrameGiver plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof ItemFrame)) {
            return;
        }

        ItemFrame itemFrame = (ItemFrame) e.getRightClicked();
        System.out.println("Clicked ID: " + itemFrame.getUniqueId());

        GiverFrame giverFrame = plugin.getFrameManager().getFrame(itemFrame.getUniqueId());
        if (giverFrame == null) {
            return;
        }

        e.setCancelled(true);

        PlayerCooldowns cooldowns = plugin.getCooldownManager().getCooldowns(e.getPlayer().getUniqueId());

        long now = System.currentTimeMillis();
        long last = cooldowns.getLastInteraction(giverFrame.getUniqueId());
        long diff = now - last;
        long cooldown = Math.round(giverFrame.getCooldown() * 1000.0);

        if (diff < cooldown) {
            e.getPlayer().sendMessage(plugin.tr("item.on-cooldown", (cooldown - diff) / 1000.0));
            return;
        }

        Map<Integer, ItemStack> leftover = e.getPlayer().getInventory().addItem(itemFrame.getItem());
        if (!leftover.isEmpty()) {
            Location location = e.getPlayer().getLocation();
            leftover.values().forEach(item -> location.getWorld().dropItem(location, item));
        }

        cooldowns.setLastInteraction(giverFrame.getUniqueId(), now);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        if (!(e.getEntity() instanceof ItemFrame)) {
            return;
        }

        ItemFrame itemFrame = (ItemFrame) e.getEntity();

        GiverFrame giverFrame = plugin.getFrameManager().getFrame(itemFrame.getUniqueId());
        if (giverFrame == null) {
            return;
        }

        if (cancelIfNotAdmin(e, e.getRemover())) {
            plugin.getFrameManager().removeFrame(giverFrame.getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntityType() != EntityType.ITEM_FRAME) {
            return;
        }

        ItemFrame itemFrame = (ItemFrame) e.getEntity();

        GiverFrame giverFrame = plugin.getFrameManager().getFrame(itemFrame.getUniqueId());
        if (giverFrame == null) {
            return;
        }

        cancelIfNotAdmin(e, e.getDamager());
    }

    private boolean cancelIfNotAdmin(Cancellable event, Entity entity) {
        if (entity instanceof Permissible) {
            Permissible permissible = (Permissible) entity;
            if (permissible.hasPermission("ItemFrameGiver.admin")) {
                return false;
            }
        }

        event.setCancelled(true);
        return true;
    }
}

package me.rayzr522.itemframegiver.command;

import me.rayzr522.itemframegiver.ItemFrameGiver;
import me.rayzr522.itemframegiver.data.GiverFrame;
import me.rayzr522.itemframegiver.utils.EntityUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class CommandHandler implements CommandExecutor {
    private ItemFrameGiver plugin;

    private Map<String, SimpleCommandExecutor> commandExecutorMap = new HashMap<>();

    public CommandHandler(ItemFrameGiver plugin) {
        this.plugin = plugin;

        addCommand("setCooldown", this::commandSetCooldown);
        addCommand("place", this::commandPlace);
    }


    private void addCommand(String subCommand, SimpleCommandExecutor executor) {
        commandExecutorMap.put(subCommand, executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.fail.only-players"));
            return true;
        }

        if (!plugin.checkPermission(sender, "admin", true)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            showUsage(player);
            return true;
        }

        SimpleCommandExecutor executor = commandExecutorMap.get(args[0]);
        if (executor == null) {
            showUsage(player);
            return true;
        }

        try {
            executor.handle(player, shift(args));
        } catch (CommandException exception) {
            player.sendMessage(plugin.tr(String.format(
                    "command.%s.%s",
                    args[0],
                    exception.getKey()
            )));
        }

        return true;
    }

    private String[] shift(String[] input) {
        if (input.length <= 1) {
            return new String[]{};
        }
        return Arrays.copyOfRange(input, 1, input.length);
    }

    private void showUsage(Player player) {
        player.sendMessage(plugin.trRaw("command.usage"));
    }

    private void commandSetCooldown(Player player, String[] args) {
        if (args.length < 1) {
            throw new CommandException("usage");
        }

        Entity entity = EntityUtils.getTargetEntity(player, 10, EntityType.ITEM_FRAME);
        if (entity == null) {
            throw new CommandException("no-target");
        }

        ItemFrame itemFrame = (ItemFrame) entity;
        GiverFrame giverFrame = plugin.getFrameManager().getFrame(itemFrame.getUniqueId());

        double cooldown;
        if (args[0].equalsIgnoreCase("none")) {
            cooldown = 0.0;
        } else {
            try {
                cooldown = Double.parseDouble(args[0]);
            } catch (NumberFormatException e) {
                throw new CommandException("invalid-amount");
            }
        }

        if (cooldown < 0) {
            throw new CommandException("invalid-amount");
        }
        giverFrame.setCooldown(cooldown);

        player.sendMessage(plugin.tr("command.setCooldown.success", cooldown));
    }

    private void commandPlace(Player player, String[] args) {
        @SuppressWarnings("deprecation")
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            throw new CommandException("needs-item");
        }

        Block block = player.getTargetBlock((Set<Material>) null, 10);
        BlockFace direction = EntityUtils.getHorizontalFaceFromLocation(player.getLocation()).getOppositeFace();

        ItemFrame itemFrame = player.getWorld().spawn(block.getRelative(direction).getLocation(), ItemFrame.class);

        itemFrame.setFacingDirection(direction);
        itemFrame.setItem(item.clone());

        // BECAUSE BUKKIT IS A RETARD, SOMEHOW MY ENTITY'S UUID IS CHANGING
        // ONE TICK AFTER CREATING IT. PLEASE SHOOT ME NOW.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (itemFrame.isDead()) {
                    return;
                }

                GiverFrame giverFrame = new GiverFrame(itemFrame.getUniqueId());

                plugin.getFrameManager().addFrame(giverFrame);

                player.sendMessage(plugin.tr("command.place.success"));
            }
        }.runTaskLater(plugin, 1L);
    }

}

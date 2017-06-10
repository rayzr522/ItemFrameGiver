package me.rayzr522.itemframegiver.command;

import org.bukkit.entity.Player;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public interface SimpleCommandExecutor {
    void handle(Player player, String[] args);
}

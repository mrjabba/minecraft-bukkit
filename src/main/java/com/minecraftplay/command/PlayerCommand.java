package com.minecraftplay.command;

import org.bukkit.entity.Player;

public interface PlayerCommand {
    boolean execute(Player player, String[] args);
}

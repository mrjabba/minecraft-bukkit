package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MoveCommand implements PlayerCommand {
    private final java.util.logging.Logger logger;

    public MoveCommand(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Location base = player.getLocation();
        World world = player.getWorld();
        logger.info("starting...placing blocks");
        for (int i = 0; i < 5; i++) {
            Location blockLoc = base.clone().add(i, 0, 0);
            world.getBlockAt(blockLoc).setType(Material.DIRT);
        }
        logger.info("starting...done?");
        return true;
    }
}

package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BuildTowerSandCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        int height = 10;
        if (args.length == 0) {
            player.sendMessage("Usage: /buildtowersand <height>");
            return false;
        }
        if (args.length >= 1) {
            try {
                height = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }

        Location base = player.getLocation().add(-3, 0, 0);
        World world = player.getWorld();

        Material blockType = Material.SAND;

        for (int y = 0; y < height; y++) {
            Location blockLoc = base.clone().add(0, y, 0);
            world.getBlockAt(blockLoc).setType(blockType);
        }

        player.sendMessage("Sand Tower built!");
        return true;
    }
}

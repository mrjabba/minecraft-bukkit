package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BuildTowerCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /buildtowersand <height> <material>");
            return false;
        }
        if (args.length >= 2) {
            Location base = player.getLocation().add(-2, 0, 0);
            World world = player.getWorld();

            Material blockType;
            int height = 10;
            try {
                height = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid height. Using default: 10");
            }
            String matName = args[1].toUpperCase();
            blockType = Material.matchMaterial(matName);
            if (blockType == null || !blockType.isBlock()) {
                player.sendMessage("Invalid material '" + args[1] + "'. Using STONE.");
                blockType = Material.STONE;
            }

            for (int y = 0; y < height; y++) {
                Location blockLoc = base.clone().add(0, y, 0);
                world.getBlockAt(blockLoc).setType(blockType);
            }
        }

        player.sendMessage("Tower built!");
        return true;
    }
}

package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class FillAreaBlockCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        Location playerLoc = player.getLocation();
        World world = player.getWorld();

        if (args.length < 3) {
            player.sendMessage("Usage: /fillareablock <radius> <height> <material>");
            return false;
        }

        int radius, height;
        try {
            radius = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid radius or height. Using default: 0");
            return false;
        }

        String matName = args[2].toUpperCase();
        Material blockType = Material.matchMaterial(matName);
        if (blockType == null || !blockType.isBlock()) {
            player.sendMessage("Invalid material '" + args[2] + "'. Using DIRT.");
            blockType = Material.DIRT;
        }

        BlockFace face = PlaceRailCommand.yawToFace(playerLoc.getYaw());

        int centerX = playerLoc.getBlockX() + face.getModX() * (radius + 1);
        int centerY = playerLoc.getBlockY();
        int centerZ = playerLoc.getBlockZ() + face.getModZ() * (radius + 1);

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    int blockX, blockZ;
                    if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                        blockX = centerX + x;
                        blockZ = centerZ + z;
                    } else {
                        blockX = centerX + z;
                        blockZ = centerZ + x;
                    }
                    Location blockLoc = new Location(world, blockX, centerY + y, blockZ);
                    world.getBlockAt(blockLoc).setType(blockType);
                }
            }
        }

        player.sendMessage("Filled a block of radius " + radius + ", height " + height + " with " + blockType.name() + " in front of you.");
        return true;
    }
}

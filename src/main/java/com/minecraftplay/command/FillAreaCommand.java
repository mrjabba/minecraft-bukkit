package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class FillAreaCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        int radius = 0;
        if (args.length < 2) {
            player.sendMessage("Usage: /fillarea <radius> <material>");
            return false;
        }
        if (args.length >= 2) {
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }
        String matName = args[1].toUpperCase();
        Material blockType = Material.matchMaterial(matName);
        if (blockType == null || !blockType.isBlock()) {
            player.sendMessage("Invalid material '" + args[1] + "'. Using DIRT.");
            blockType = Material.DIRT;
        }

        int yLevel = center.getBlockY();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLoc = new Location(world, center.getX() + x, yLevel, center.getZ() + z);
                world.getBlockAt(blockLoc).setType(blockType);
            }
        }

        player.sendMessage("Filled a " + (radius * 2) + "x" + (radius * 2) + " area with " + blockType.name() + ".");
        return true;
    }
}

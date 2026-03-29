package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ClearAreaCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        int radius = 0;
        if (args.length == 0) {
            player.sendMessage("Usage: /clearArea <radius>");
            return false;
        }
        if (args.length >= 1) {
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }

        int yLevel = center.getBlockY();
        Material targetBlock = Material.DIRT;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLoc = new Location(world, center.getX() + x, yLevel, center.getZ() + z);
                if (world.getBlockAt(blockLoc).getType() == targetBlock) {
                    world.getBlockAt(blockLoc).setType(Material.AIR);
                }
            }
        }

        player.sendMessage("Cleared dirt in a " + (radius * 2) + "x" + (radius * 2) + " area.");
        return true;
    }
}

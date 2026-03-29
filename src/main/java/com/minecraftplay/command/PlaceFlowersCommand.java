package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlaceFlowersCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /placeflowers <flowerType> <radius>");
            return true;
        }

        String flowerTypeArg = args[0].toUpperCase();
        Material flowerType = Material.matchMaterial(flowerTypeArg);
        if (flowerType == null || !flowerType.isBlock()) {
            player.sendMessage("Invalid flower type: " + args[0]);
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid radius: " + args[1]);
            return true;
        }
        if (radius < 1 || radius > 10) {
            player.sendMessage("Radius must be between 1 and 10.");
            return true;
        }

        Location playerLoc = player.getLocation();
        World world = player.getWorld();
        org.bukkit.util.Vector direction = playerLoc.getDirection().normalize();

        Location center = playerLoc.clone().add(direction.multiply(3));
        center.setY(world.getHighestBlockYAt(center) + 1);

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                Location flowerLoc = center.clone().add(dx, 0, dz);
                Block below = world.getBlockAt(flowerLoc.clone().add(0, -1, 0));
                Block flowerBlock = world.getBlockAt(flowerLoc);
                if (below.getType().isSolid() && flowerBlock.getType() == Material.AIR) {
                    flowerBlock.setType(flowerType);
                }
            }
        }

        player.sendMessage("You placed flowers in front of you!");
        return true;
    }
}

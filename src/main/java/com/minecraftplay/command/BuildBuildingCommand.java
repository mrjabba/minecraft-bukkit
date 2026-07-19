package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BuildBuildingCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {

        int radius, height;
        try {
            radius = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid radius or height. Using default: 0");
            return false;
        }

        emptyBlockWithAir(player, args, radius, height);
        Location interiorCenter = movePlayerToCenterOfBlock(player, radius);
        placeItemAtCornersOfBlock(interiorCenter, radius, Material.LANTERN);

        return true;
    }
    
    private void flattenPlayerDirection(Vector direction) {
        direction.setY(0); // Kill the vertical component so the floor stays level

        // Safety check: if the player looks directly straight up or down, 
        // the horizontal length becomes 0. We handle that to prevent errors.
        if (direction.lengthSquared() > 0) {
            direction.normalize();
        } else {
            // Default fallback direction (e.g., forward along Z) if looking dead up/down
            direction = new Vector(0, 0, 1); 
        }
   }

   private void emptyBlockWithAir(Player player, String[] args, int radius, int height) {
        // execute our existing command
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        command.execute(player, args);

        // move player forward by 1 block horizontally
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        flattenPlayerDirection(direction);

        Location targetLoc = loc.add(direction.multiply(1));
        player.teleport(targetLoc);

        // file inside of block with AIR to make the block hollow
        args[0] = String.valueOf(radius - 1);
        args[1] = String.valueOf(height - 1);
        args[2] = Material.AIR.toString();
        command.execute(player, args);
        player.sendMessage("Emptied block to make an empty building.");
   }

   private Location movePlayerToCenterOfBlock(Player player, int radius) {
        player.sendMessage("sunday debug.");
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();

        // 1. Convert the looking angle into a strict grid direction (1 or -1)
        int dx = 0;
        int dz = 0;

        if (Math.abs(direction.getX()) > Math.abs(direction.getZ())) {
            // Player is looking predominantly East or West
            dx = direction.getX() > 0 ? 1 : -1;
        } else {
            // Player is looking predominantly North or South
            dz = direction.getZ() > 0 ? 1 : -1;
        }

        // 2. Calculate the exact center block using integer math
        // Adding 0.5 keeps the player perfectly in the middle of the block when they teleport
        double centerX = loc.getBlockX() + (dx * radius) + 0.5;
        double centerY = loc.getBlockY();
        double centerZ = loc.getBlockZ() + (dz * radius) + 0.5;

        Location interiorCenter = new Location(player.getWorld(), centerX, centerY, centerZ);

        // Set the player's yaw/pitch so they don't look a random direction when teleported
        interiorCenter.setYaw(loc.getYaw());
        interiorCenter.setPitch(loc.getPitch());

        player.teleport(interiorCenter);
        return interiorCenter;
   }

   private void placeItemAtCornersOfBlock(Location interiorCenter, int radius, Material material) {
        // Calculate the safe interior radius
        int innerRadius = radius - 1;

        int[][] cornerOffsets = {
            {innerRadius, innerRadius},   // Top Right
            {innerRadius, -innerRadius},  // Bottom Right
            {-innerRadius, innerRadius},  // Top Left
            {-innerRadius, -innerRadius}  // Bottom Left
        };

        for (int[] offset : cornerOffsets) {
            // Target the floor level inside the room
            Location lanternLoc = interiorCenter.clone().add(offset[0], 0, offset[1]);
            Block targetBlock = lanternLoc.getBlock();
            
            // SAFETY CHECK 1: Only place if the target block is currently AIR
            if (targetBlock.getType() == Material.AIR) {
                targetBlock.setType(material);
            } else {
                // FALLBACK: If it's NOT air (meaning it's part of the wall), 
                // shift 1 block inward toward the center of the room to protect the wall.
                int safeX = offset[0] > 0 ? offset[0] - 1 : offset[0] + 1;
                int safeZ = offset[1] > 0 ? offset[1] - 1 : offset[1] + 1;
                
                Location safeLoc = interiorCenter.clone().add(safeX, 0, safeZ);
                if (safeLoc.getBlock().getType() == Material.AIR) {
                    safeLoc.getBlock().setType(material);
                }
            }
        }    
   }
}

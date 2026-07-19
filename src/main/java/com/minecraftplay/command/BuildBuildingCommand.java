package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        
        // Decorate the building with the base kit
        placeInteriorFurniture(interiorCenter, radius);

        placeOppositeDoors(interiorCenter, radius);
        
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

    private void placeInteriorFurniture(Location interiorCenter, int radius) {
        int innerRadius = radius - 1;

        // DEFENSIVE SCAN: Dynamically find the exact interior face of the East (+X) and West (-X) walls.
        // We start at the theoretical boundary and step inward until we hit actual AIR.
        int safeEastX = innerRadius;
        while (safeEastX > 0 && interiorCenter.clone().add(safeEastX, 0, 0).getBlock().getType() != Material.AIR) {
            safeEastX--;
        }

        int safeWestX = -innerRadius;
        while (safeWestX < 0 && interiorCenter.clone().add(safeWestX, 0, 0).getBlock().getType() != Material.AIR) {
            safeWestX++;
        }

        // 1. Utility Line (Flush against the East Wall)
        // Since they sit on the East wall (+X), they should face WEST to look into the room
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 0), Material.CRAFTING_TABLE, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 1), Material.CHEST, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 2), Material.CHEST, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, -1), Material.FURNACE, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, -2), Material.FURNACE, BlockFace.WEST);

        // 2. Bed Assembly Line (Flush against the West Wall)
        Location bedFootLoc = interiorCenter.clone().add(safeWestX, 0, 0);
        Location bedHeadLoc = interiorCenter.clone().add(safeWestX, 0, 1);

        Block footBlock = bedFootLoc.getBlock();
        Block headBlock = bedHeadLoc.getBlock();

        // DOUBLE-GUARD: Ensure both targets are AIR so a tiny house radius doesn't overwrite a corner lantern
        if (footBlock.getType() == Material.AIR && headBlock.getType() == Material.AIR) {
            footBlock.setType(Material.RED_BED, false);
            headBlock.setType(Material.RED_BED, false);

            if (footBlock.getBlockData() instanceof org.bukkit.block.data.type.Bed) {
                org.bukkit.block.data.type.Bed footData = (org.bukkit.block.data.type.Bed) footBlock.getBlockData();
                footData.setPart(org.bukkit.block.data.type.Bed.Part.FOOT);
                footData.setFacing(BlockFace.SOUTH); // Head block is 1 block South (+Z)
                footBlock.setBlockData(footData, false);

                org.bukkit.block.data.type.Bed headData = (org.bukkit.block.data.type.Bed) headBlock.getBlockData();
                headData.setPart(org.bukkit.block.data.type.Bed.Part.HEAD);
                headData.setFacing(BlockFace.SOUTH);
                headBlock.setBlockData(headData, false);
            }
        }
   }

    // Helper method to safely handle block placement and structural rotation
    private void placeDirectionalComponent(Location loc, Material material, BlockFace facing) {
        Block block = loc.getBlock();
        
        // SAFETY CHECK: Never overwrite something that isn't empty space
        if (block.getType() == Material.AIR) {
            block.setType(material, false);
            
            // Check if the block type supports rotation (like Chests and Furnaces do)
            if (block.getBlockData() instanceof org.bukkit.block.data.Directional) {
                org.bukkit.block.data.Directional directional = (org.bukkit.block.data.Directional) block.getBlockData();
                directional.setFacing(facing);
                block.setBlockData(directional, false);
            }
        }
    }

    private void placeOppositeDoors(Location interiorCenter, int radius) {
        int innerRadius = radius - 1;

        // DEFENSIVE SCAN: Find the exact North and South wall coordinates on the Z-axis
        int safeSouthZ = innerRadius;
        while (safeSouthZ > 0 && interiorCenter.clone().add(0, 0, safeSouthZ).getBlock().getType() != Material.AIR) {
            safeSouthZ--;
        }
        int southWallZ = safeSouthZ + 1; // The actual solid wall block

        int safeNorthZ = -innerRadius;
        while (safeNorthZ < 0 && interiorCenter.clone().add(0, 0, safeNorthZ).getBlock().getType() != Material.AIR) {
            safeNorthZ++;
        }
        int northWallZ = safeNorthZ - 1; // The actual solid wall block

        // 1. South Door (Centered at X=0, placed into the South Wall, facing outwards)
        Location southDoorLoc = interiorCenter.clone().add(0, 0, southWallZ);
        placeDoor(southDoorLoc, BlockFace.SOUTH);

        // 2. North Door (Centered at X=0, placed into the North Wall, facing outwards)
        Location northDoorLoc = interiorCenter.clone().add(0, 0, northWallZ);
        placeDoor(northDoorLoc, BlockFace.NORTH);
   }

   private void placeDoor(Location lowerLoc, BlockFace facing) {
        Block bottomBlock = lowerLoc.getBlock();
        Block topBlock = lowerLoc.clone().add(0, 1, 0).getBlock();

        // Force set the blocks to an Oak Door without physics to prevent them from breaking instantly
        bottomBlock.setType(Material.OAK_DOOR, false);
        topBlock.setType(Material.OAK_DOOR, false);

        // Apply BlockData to the bottom half
        if (bottomBlock.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            org.bukkit.block.data.type.Door bottomData = (org.bukkit.block.data.type.Door) bottomBlock.getBlockData();
            bottomData.setHalf(org.bukkit.block.data.Bisected.Half.BOTTOM);
            bottomData.setFacing(facing);
            bottomBlock.setBlockData(bottomData, false);
        }

        // Apply BlockData to the top half
        if (topBlock.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            org.bukkit.block.data.type.Door topData = (org.bukkit.block.data.type.Door) topBlock.getBlockData();
            topData.setHalf(org.bukkit.block.data.Bisected.Half.TOP);
            topData.setFacing(facing);
            topBlock.setBlockData(topData, false);
        }
   }
}
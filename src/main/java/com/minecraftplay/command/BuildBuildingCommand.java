package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BuildBuildingCommand implements PlayerCommand {
    
    // Define our strict architectural guardrails
    private static final int MIN_RADIUS = 8;
    private static final int MIN_HEIGHT = 6;

    @Override
    public boolean execute(Player player, String[] args) {
        // Quick syntax safety check
        if (args.length < 2) {
            player.sendMessage("Usage: /buildbuilding <radius> <height> [material]");
            return false;
        }

        int radius, height;
        try {
            radius = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid radius or height. Please use whole numbers.");
            return false;
        }

        // DEFENSIVE GUARDRAIL: Enforce minimum dimensions for large buildings
        if (radius < MIN_RADIUS || height < MIN_HEIGHT) {
            player.sendMessage("Structure too small! This command requires a minimum radius of " 
                + MIN_RADIUS + " and height of " + MIN_HEIGHT + ". Please use BuildHouseCommand instead.");
            return false;
        }

        // Structural Phase
        emptyBlockWithAir(player, args, radius, height);
        Location interiorCenter = movePlayerToCenterOfBlock(player, radius);
        
        // Decoration Phase
        placeItemAtCornersOfBlock(interiorCenter, radius, Material.LANTERN);
        placeInteriorFurniture(interiorCenter, radius);
        placeOppositeDoors(interiorCenter, radius);
        placeSideWallWindows(interiorCenter, radius);
        return true;
    }
    
    private void flattenPlayerDirection(Vector direction) {
        direction.setY(0);
        if (direction.lengthSquared() > 0) {
            direction.normalize();
        } else {
            direction = new Vector(0, 0, 1); 
        }
   }

   private void emptyBlockWithAir(Player player, String[] args, int radius, int height) {
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        command.execute(player, args);

        Location loc = player.getLocation();
        Vector direction = loc.getDirection();
        flattenPlayerDirection(direction);

        Location targetLoc = loc.add(direction.multiply(1));
        player.teleport(targetLoc);

        args[0] = String.valueOf(radius - 1);
        args[1] = String.valueOf(height - 1);
        args[2] = Material.AIR.toString();
        command.execute(player, args);
        player.sendMessage("Emptied block to make an empty building.");
   }

   private Location movePlayerToCenterOfBlock(Player player, int radius) {
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();

        int dx = 0;
        int dz = 0;

        if (Math.abs(direction.getX()) > Math.abs(direction.getZ())) {
            dx = direction.getX() > 0 ? 1 : -1;
        } else {
            dz = direction.getZ() > 0 ? 1 : -1;
        }

        double centerX = loc.getBlockX() + (dx * radius) + 0.5;
        double centerY = loc.getBlockY();
        double centerZ = loc.getBlockZ() + (dz * radius) + 0.5;

        Location interiorCenter = new Location(player.getWorld(), centerX, centerY, centerZ);
        interiorCenter.setYaw(loc.getYaw());
        interiorCenter.setPitch(loc.getPitch());

        player.teleport(interiorCenter);
        return interiorCenter;
   }

   private void placeItemAtCornersOfBlock(Location interiorCenter, int radius, Material material) {
        int innerRadius = radius - 1;

        int[][] cornerOffsets = {
            {innerRadius, innerRadius},   
            {innerRadius, -innerRadius},  
            {-innerRadius, innerRadius},  
            {-innerRadius, -innerRadius}  
        };

        for (int[] offset : cornerOffsets) {
            Location lanternLoc = interiorCenter.clone().add(offset[0], 0, offset[1]);
            Block targetBlock = lanternLoc.getBlock();
            
            if (targetBlock.getType() == Material.AIR) {
                targetBlock.setType(material);
            } else {
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

        int safeEastX = innerRadius;
        while (safeEastX > 0 && interiorCenter.clone().add(safeEastX, 0, 0).getBlock().getType() != Material.AIR) {
            safeEastX--;
        }

        int safeWestX = -innerRadius;
        while (safeWestX < 0 && interiorCenter.clone().add(safeWestX, 0, 0).getBlock().getType() != Material.AIR) {
            safeWestX++;
        }

        // Utility Line (East Wall, Facing West)
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 0), Material.CRAFTING_TABLE, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 1), Material.CHEST, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, 2), Material.CHEST, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, -1), Material.FURNACE, BlockFace.WEST);
        placeDirectionalComponent(interiorCenter.clone().add(safeEastX, 0, -2), Material.FURNACE, BlockFace.WEST);

        // Bed Line (West Wall, Facing South)
        Location bedFootLoc = interiorCenter.clone().add(safeWestX, 0, 0);
        Location bedHeadLoc = interiorCenter.clone().add(safeWestX, 0, 1);

        Block footBlock = bedFootLoc.getBlock();
        Block headBlock = bedHeadLoc.getBlock();

        if (footBlock.getType() == Material.AIR && headBlock.getType() == Material.AIR) {
            footBlock.setType(Material.RED_BED, false);
            headBlock.setType(Material.RED_BED, false);

            if (footBlock.getBlockData() instanceof org.bukkit.block.data.type.Bed) {
                org.bukkit.block.data.type.Bed footData = (org.bukkit.block.data.type.Bed) footBlock.getBlockData();
                footData.setPart(org.bukkit.block.data.type.Bed.Part.FOOT);
                footData.setFacing(BlockFace.SOUTH); 
                footBlock.setBlockData(footData, false);

                org.bukkit.block.data.type.Bed headData = (org.bukkit.block.data.type.Bed) headBlock.getBlockData();
                headData.setPart(org.bukkit.block.data.type.Bed.Part.HEAD);
                headData.setFacing(BlockFace.SOUTH);
                headBlock.setBlockData(headData, false);
            }
        }
   }

   private void placeDirectionalComponent(Location loc, Material material, BlockFace facing) {
        Block block = loc.getBlock();
        if (block.getType() == Material.AIR) {
            block.setType(material, false);
            if (block.getBlockData() instanceof org.bukkit.block.data.Directional) {
                org.bukkit.block.data.Directional directional = (org.bukkit.block.data.Directional) block.getBlockData();
                directional.setFacing(facing);
                block.setBlockData(directional, false);
            }
        }
   }

   private void placeOppositeDoors(Location interiorCenter, int radius) {
        int innerRadius = radius - 1;

        int safeSouthZ = innerRadius;
        while (safeSouthZ > 0 && interiorCenter.clone().add(0, 0, safeSouthZ).getBlock().getType() != Material.AIR) {
            safeSouthZ--;
        }
        int southWallZ = safeSouthZ + 1; 

        int safeNorthZ = -innerRadius;
        while (safeNorthZ < 0 && interiorCenter.clone().add(0, 0, safeNorthZ).getBlock().getType() != Material.AIR) {
            safeNorthZ++;
        }
        int northWallZ = safeNorthZ - 1; 

        // South Door & Windows
        Location southDoorLoc = interiorCenter.clone().add(0, 0, southWallZ);
        placeDoor(southDoorLoc, BlockFace.SOUTH);
        placeWindow(southDoorLoc.clone().add(-1, 1, 0));
        placeWindow(southDoorLoc.clone().add(1, 1, 0));

        // North Door & Windows
        Location northDoorLoc = interiorCenter.clone().add(0, 0, northWallZ);
        placeDoor(northDoorLoc, BlockFace.NORTH);
        placeWindow(northDoorLoc.clone().add(-1, 1, 0));
        placeWindow(northDoorLoc.clone().add(1, 1, 0));
   }

   private void placeDoor(Location lowerLoc, BlockFace facing) {
        Block bottomBlock = lowerLoc.getBlock();
        Block topBlock = lowerLoc.clone().add(0, 1, 0).getBlock();

        bottomBlock.setType(Material.OAK_DOOR, false);
        topBlock.setType(Material.OAK_DOOR, false);

        if (bottomBlock.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            org.bukkit.block.data.type.Door bottomData = (org.bukkit.block.data.type.Door) bottomBlock.getBlockData();
            bottomData.setHalf(org.bukkit.block.data.Bisected.Half.BOTTOM);
            bottomData.setFacing(facing);
            bottomBlock.setBlockData(bottomData, false);
        }

        if (topBlock.getBlockData() instanceof org.bukkit.block.data.type.Door) {
            org.bukkit.block.data.type.Door topData = (org.bukkit.block.data.type.Door) topBlock.getBlockData();
            topData.setHalf(org.bukkit.block.data.Bisected.Half.TOP);
            topData.setFacing(facing);
            topBlock.setBlockData(topData, false);
        }
   }

    private void placeSideWallWindows(Location interiorCenter, int radius) {
        int innerRadius = radius - 1;

        // 1. EAST WALL WINDOW (4 blocks wide, 3 blocks high)
        int safeEastX = innerRadius;
        while (safeEastX > 0 && interiorCenter.clone().add(safeEastX, 2, 0).getBlock().getType() != Material.AIR) {
            safeEastX--;
        }
        int eastWallX = safeEastX + 1; 

        // Vertical loop (Height: 2 to 4)
        for (int yOffset = 2; yOffset <= 4; yOffset++) {
            // Horizontal loop along the wall (Width: 4 blocks total)
            for (int zOffset = -2; zOffset <= 1; zOffset++) {
                Block block = interiorCenter.clone().add(eastWallX, yOffset, zOffset).getBlock();
                block.setType(Material.GLASS, true); 
            }
        }

        // 2. WEST WALL WINDOW (4 blocks wide, 3 blocks high)
        int safeWestX = -innerRadius;
        while (safeWestX < 0 && interiorCenter.clone().add(safeWestX, 2, 0).getBlock().getType() != Material.AIR) {
            safeWestX++;
        }
        int westWallX = safeWestX - 1; 

        // Vertical loop (Height: 2 to 4)
        for (int yOffset = 2; yOffset <= 4; yOffset++) {
            // Horizontal loop along the wall (Width: 4 blocks total)
            for (int zOffset = -2; zOffset <= 1; zOffset++) {
                Block block = interiorCenter.clone().add(westWallX, yOffset, zOffset).getBlock();
                block.setType(Material.GLASS, true);
            }
        }
   }

   private void placeWindow(Location loc) {
            Block block = loc.getBlock();
            // Only replace solid wall framing, don't overwrite air or doors
            if (block.getType() != Material.AIR && block.getType() != Material.OAK_DOOR) {
                block.setType(Material.GLASS, true); 
            }
    }   
}
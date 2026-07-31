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
        try {
            // 1. Parse and validate all inputs into our clean Record object
            BuildingParameters params = BuildingParameters.fromArgs(args);

            // 2. Structural Phase
            // params.toShellArgs() provides the exact [radius, height, material] array it expects
            emptyBlockWithAir(player, params.toShellArgs(), params.radius(), params.height());
            
            // Relocate player to the center of the structure
            Location interiorCenter = movePlayerToCenterOfBlock(player, params.radius());
            
            // 3. Decoration Phase 
            // Updated to pass height for the ceiling lanterns
            placeItemAtCornersOfBlock(interiorCenter, params.radius(), params.height(), Material.LANTERN);
            placeInteriorFurniture(interiorCenter, params.radius());
            placeOppositeDoors(interiorCenter, params.radius());
            placeSideWallWindows(interiorCenter, params.radius());
            
            // 4. Roofing Phase
            if (params.peakedRoof()) {
                buildPeakedRoof(interiorCenter, params.radius(), params.height());
            }

            return true;

        } catch (IllegalArgumentException e) {
            // Captures missing arguments, bad numbers, or invalid materials safely
            player.sendMessage(e.getMessage());
            return false;
        }
    }

    private void emptyBlockWithAir(Player player, String[] args, int radius, int height) {
        FillAreaBlockCommand command = new FillAreaBlockCommand();

        // 1. Build solid structure centered at: PlayerPos + (CardinalDir * radius)
        command.execute(player, args);

        // 2. Snap player facing direction to cardinal grid (matching movePlayerToCenterOfBlock)
        Location loc = player.getLocation();
        Vector direction = loc.getDirection();

        int dx = 0;
        int dz = 0;

        if (Math.abs(direction.getX()) > Math.abs(direction.getZ())) {
            dx = direction.getX() > 0 ? 1 : -1;
        } else {
            dz = direction.getZ() > 0 ? 1 : -1;
        }

        // 3. Teleport player EXACTLY 1 full block along the cardinal axis.
        // (PlayerPos + 1) + (radius - 1) = PlayerPos + radius (Centers stay identical!)
        Location targetLoc = loc.clone().add(dx, 0, dz);
        player.teleport(targetLoc);

        // 4. Clear interior with AIR
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

    private void placeItemAtCornersOfBlock(Location interiorCenter, int radius, int height, Material material) {
        int innerRadius = radius - 1;
        int ceilingY = height - 2; 

        int[][] cornerOffsets = {
            {innerRadius, innerRadius},   
            {innerRadius, -innerRadius},  
            {-innerRadius, innerRadius},  
            {-innerRadius, -innerRadius}  
        };

        for (int[] offset : cornerOffsets) {
            // 1. Ground Lanterns (Sitting on the floor)
            Location groundLoc = interiorCenter.clone().add(offset[0], 0, offset[1]);
            setLanternBlock(groundLoc, material, false, interiorCenter, offset, 0);

            // 2. Ceiling Lanterns (Hanging down from the ceiling)
            Location ceilingLoc = interiorCenter.clone().add(offset[0], ceilingY, offset[1]);
            setLanternBlock(ceilingLoc, material, true, interiorCenter, offset, ceilingY);
        }    
    }

   private void setLanternBlock(Location loc, Material material, boolean hanging, Location interiorCenter, int[] offset, int yOffset) {
        Block block = loc.getBlock();
        
        if (block.getType() == Material.AIR) {
            block.setType(material, false);
            applyLanternData(block, hanging);
        } else {
            // Safety fallback offset adjustment if the corner is obstructed
            int safeX = offset[0] > 0 ? offset[0] - 1 : offset[0] + 1;
            int safeZ = offset[1] > 0 ? offset[1] - 1 : offset[1] + 1;
            
            Location safeLoc = interiorCenter.clone().add(safeX, yOffset, safeZ);
            Block safeBlock = safeLoc.getBlock();
            if (safeBlock.getType() == Material.AIR) {
                safeBlock.setType(material, false);
                applyLanternData(safeBlock, hanging);
            }
        }
   }

   private void applyLanternData(Block block, boolean hanging) {
        if (block.getBlockData() instanceof org.bukkit.block.data.type.Lantern) {
            org.bukkit.block.data.type.Lantern lanternData = (org.bukkit.block.data.type.Lantern) block.getBlockData();
            lanternData.setHanging(hanging);
            block.setBlockData(lanternData, false);
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

   private void buildPeakedRoof(Location interiorCenter, int radius, int height) {
        // Defensive Scan: Find the material type of the wall block so we can match the gable end colors
        Material wallMaterial = interiorCenter.clone().add(radius, 0, 0).getBlock().getType();
        if (wallMaterial == Material.AIR) {
            wallMaterial = Material.BROWN_TERRACOTTA; // Fallback structural material
        }

        // Run the roof along the entire depth of the building on the Z axis
        for (int z = -radius; z <= radius; z++) {
            
            // Step inward on the X axis from the outer edges to the center line
            for (int x = 1; x <= radius; x++) {
                // As X moves closer to 0, height increases by 1
                int yOffset = height + (radius - x);

                // East Slope (Stairs face East so they descend toward the East wall)
                Location eastStair = interiorCenter.clone().add(x, yOffset, z);
                placeStair(eastStair, Material.OAK_STAIRS, BlockFace.EAST);

                // West Slope (Stairs face West so they descend toward the West wall)
                Location westStair = interiorCenter.clone().add(-x, yOffset, z);
                placeStair(westStair, Material.OAK_STAIRS, BlockFace.WEST);

                // GABLE END WALL FILL: If we are on the front or back edge, seal the triangle gap under the stairs
                if (z == -radius || z == radius) {
                    for (int fillY = height; fillY < yOffset; fillY++) {
                        interiorCenter.clone().add(x, fillY, z).getBlock().setType(wallMaterial, false);
                        interiorCenter.clone().add(-x, fillY, z).getBlock().setType(wallMaterial, false);
                    }
                }
            }

            // Center Ridge Peak (Caps off the top where the East and West slopes meet at X=0)
            int peakY = height + radius;
            Location ridgeLoc = interiorCenter.clone().add(0, peakY, z);
            ridgeLoc.getBlock().setType(Material.OAK_PLANKS, false);

            // Fill the remaining central gap in the front and back gable walls
            if (z == -radius || z == radius) {
                for (int fillY = height; fillY < peakY; fillY++) {
                    interiorCenter.clone().add(0, fillY, z).getBlock().setType(wallMaterial, false);
                }
            }
        }
   }

   private void placeStair(Location loc, Material material, BlockFace facing) {
        Block block = loc.getBlock();
        block.setType(material, false);
        
        // Cast to modern Bukkit Stairs BlockData to update orientation properly
        if (block.getBlockData() instanceof org.bukkit.block.data.type.Stairs) {
            org.bukkit.block.data.type.Stairs stairs = (org.bukkit.block.data.type.Stairs) block.getBlockData();
            stairs.setFacing(facing);
            block.setBlockData(stairs, false);
        }
   }
}
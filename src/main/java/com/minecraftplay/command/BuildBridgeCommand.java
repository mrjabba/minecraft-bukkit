package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class BuildBridgeCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("Usage: /buildbridge <length> <width> <blockType>");
            return true;
        }

        int length, width;
        try {
            length = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length and width must be integers.");
            return true;
        }

        if (length <= 0 || width <= 0) {
            player.sendMessage("Length and width must be greater than 0.");
            return true;
        }

        Material blockType = Material.matchMaterial(args[2].toUpperCase());
        if (blockType == null || !blockType.isBlock()) {
            player.sendMessage("Invalid material '" + args[2] + "'. Using COBBLESTONE.");
            blockType = Material.COBBLESTONE;
        }

        World world = player.getWorld();
        Location base = player.getLocation();
        BlockFace direction = player.getFacing();
        
        int startX = base.getBlockX();
        int startY = base.getBlockY();
        int startZ = base.getBlockZ();

        int maxLayers = 3;
        int waterLevel = world.getSeaLevel();
        
        int extendedLength = findBridgeEnd(world, startX, startZ, direction, length, width, waterLevel);
        int actualLength = Math.max(length, extendedLength);
        
        for (int l = 1; l <= actualLength; l++) {
            Location layerOffset = getOffset(new Location(world, startX, startY, startZ), direction, l);
            
            for (int w = 0; w < width; w++) {
                Location widthOffset = getSideOffset(layerOffset, direction, w);
                int wX = widthOffset.getBlockX();
                int wZ = widthOffset.getBlockZ();
                
                int groundY = world.getHighestBlockYAt(wX, wZ);
                
                boolean isOverWater = groundY < waterLevel;
                
                int bridgeY = isOverWater ? waterLevel : groundY;
                
                Block floorBlock = world.getBlockAt(wX, bridgeY, wZ);
                floorBlock.setType(blockType);
                
                if (isOverWater) {
                    for (int layer = 1; layer < maxLayers; layer++) {
                        int y = bridgeY - layer;
                        if (y < world.getMinHeight() || y >= world.getMaxHeight()) continue;
                        
                        Block supportBlock = world.getBlockAt(wX, y, wZ);
                        supportBlock.setType(blockType);
                    }
                }
                
                if (w == 0 || w == width - 1) {
                    int wallY = bridgeY + 1;
                    if (wallY < world.getMaxHeight()) {
                        Block wallBlock = world.getBlockAt(wX, wallY, wZ);
                        wallBlock.setType(blockType);
                    }
                }
            }
            
            if (l == 1 || l == actualLength) {
                for (int w = -1; w <= width; w += width + 1) {
                    Location widthOffset = getSideOffset(layerOffset, direction, w);
                    int wX = widthOffset.getBlockX();
                    int wZ = widthOffset.getBlockZ();
                    
                    int groundY = world.getHighestBlockYAt(wX, wZ);
                    boolean isOverWater = groundY < waterLevel;
                    int baseY = isOverWater ? waterLevel : groundY;
                    
                    Block pillarBase = world.getBlockAt(wX, baseY + 1, wZ);
                    pillarBase.setType(blockType);
                    
                    Block lanternBlock = world.getBlockAt(wX, baseY + 2, wZ);
                    lanternBlock.setType(Material.LANTERN);
                }
            }
        }
        
        if (actualLength > length) {
            player.sendMessage("Extended bridge to " + actualLength + " blocks to reach land.");
        }

        player.sendMessage("Built bridge: " + length + "x" + width + " using " + blockType.name());
        return true;
    }

    private Location getOffset(Location base, BlockFace direction, int amount) {
        return switch (direction) {
            case NORTH -> base.clone().add(0, 0, -amount);
            case SOUTH -> base.clone().add(0, 0, amount);
            case EAST -> base.clone().add(amount, 0, 0);
            case WEST -> base.clone().add(-amount, 0, 0);
            default -> base.clone().add(0, 0, amount);
        };
    }

    private Location getSideOffset(Location base, BlockFace direction, int amount) {
        return switch (direction) {
            case NORTH, SOUTH -> base.clone().add(amount, 0, 0);
            case EAST, WEST -> base.clone().add(0, 0, amount);
            default -> base.clone().add(amount, 0, 0);
        };
    }
    
    private int findBridgeEnd(World world, int startX, int startZ, BlockFace direction, int length, int width, int waterLevel) {
        int checkPos = length + 1;
        
        for (int l = checkPos; l <= checkPos + 20; l++) {
            Location layerOffset = getOffset(new Location(world, startX, 0, startZ), direction, l);
            
            boolean hasWater = false;
            for (int w = 0; w < width; w++) {
                Location widthOffset = getSideOffset(layerOffset, direction, w);
                int wX = widthOffset.getBlockX();
                int wZ = widthOffset.getBlockZ();
                
                int groundY = world.getHighestBlockYAt(wX, wZ);
                if (groundY < waterLevel) {
                    hasWater = true;
                    break;
                }
            }
            
            if (!hasWater) {
                return l;
            }
        }
        
        return length;
    }
}

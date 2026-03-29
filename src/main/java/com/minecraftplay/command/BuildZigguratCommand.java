package com.minecraftplay.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;

public class BuildZigguratCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /buildziggurat <length> <material> [stairs]");
            return false;
        }

        int length;
        Material material = Material.STONE;
        boolean buildStairs = false;

        try {
            length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid length. Using default: 10");
            length = 10;
        }

        if (length < 1) {
            player.sendMessage("Length must be at least 1.");
            return false;
        }

        Material parsedMaterial = Material.matchMaterial(args[1].toUpperCase());
        if (parsedMaterial != null && parsedMaterial.isBlock()) {
            material = parsedMaterial;
        } else {
            player.sendMessage("Invalid material '" + args[1] + "'. Using STONE.");
        }

        if (args.length >= 3) {
            String stairsArg = args[2].toLowerCase();
            buildStairs = stairsArg.equals("stairs") || stairsArg.equals("true") || stairsArg.equals("yes");
        }

        Location baseLocation = player.getLocation().getBlock().getLocation();
        int height = buildZiggurat(player.getWorld(), baseLocation, length, material, buildStairs);

        player.sendMessage("Ziggurat built! Length: " + length + "x" + length + ", Height: " + height + " levels, Material: " + material.name() + (buildStairs ? " with stairs" : ""));
        return true;
    }

    private int buildZiggurat(World world, Location baseLocation, int length, Material material, boolean buildStairs) {
        int startX = baseLocation.getBlockX();
        int startY = baseLocation.getBlockY();
        int startZ = baseLocation.getBlockZ();

        int level = 0;
        int currentSize = length;

        while (currentSize >= 1) {
            int y = startY + level;
            int offset = (length - currentSize) / 2;

            for (int x = 0; x < currentSize; x++) {
                for (int z = 0; z < currentSize; z++) {
                    world.getBlockAt(startX + offset + x, y, startZ + offset + z).setType(material);
                }
            }

            level++;
            currentSize -= 2;
        }

        if (buildStairs) {
            addZigguratStairway(world, startX, startY, startZ, length, material);
        }

        return level;
    }

    private void addZigguratStairway(World world, int startX, int startY, int startZ, int length, Material material) {
        int level = 0;
        int currentSize = length;

        Material stairsMaterial = getStairsMaterial(material);

        while (currentSize >= 1) {
            int y = startY + level;
            int offset = (length - currentSize) / 2;

            int stairX = startX + offset + currentSize / 2;

            int northStairZ = startZ + offset;
            placeStair(world, stairX, y, northStairZ, stairsMaterial, BlockFace.SOUTH);

            int southStairZ = startZ + offset + currentSize - 1;
            placeStair(world, stairX, y, southStairZ, stairsMaterial, BlockFace.NORTH);

            level++;
            currentSize -= 2;
        }
    }

    private Material getStairsMaterial(Material material) {
        try {
            String stairsName = material.name() + "_STAIRS";
            Material stairsMat = Material.valueOf(stairsName);
            if (stairsMat.isBlock()) {
                return stairsMat;
            }
        } catch (IllegalArgumentException e) {
            // Material stairs variant not found
        }
        return Material.OAK_STAIRS;
    }

    private void placeStair(World world, int x, int y, int z, Material material, BlockFace facing) {
        Block stairBlock = world.getBlockAt(x, y, z);
        stairBlock.setType(material);
        Stairs stairData = (Stairs) Bukkit.createBlockData(material);
        stairData.setFacing(facing);
        stairData.setHalf(Stairs.Half.BOTTOM);
        stairBlock.setBlockData(stairData);
    }
}

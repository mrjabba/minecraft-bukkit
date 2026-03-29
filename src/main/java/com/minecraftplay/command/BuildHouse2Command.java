package com.minecraftplay.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;

public class BuildHouse2Command implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /buildhouse2 <length> <width>");
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

        if (length <= 1 || width <= 1) {
            player.sendMessage("Length and width must be greater than 1.");
            return true;
        }

        Location base = player.getLocation().getBlock().getLocation();
        World world = base.getWorld();
        int startX = base.getBlockX();
        int startY = base.getBlockY();
        int startZ = base.getBlockZ();

        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX + x, startY - 1, startZ + z).setType(Material.OAK_PLANKS);
            }
        }

        int wallHeight = 4;
        for (int y = 0; y < wallHeight; y++) {
            for (int x = 0; x < length; x++) {
                world.getBlockAt(startX + x, startY + y, startZ).setType(Material.OAK_PLANKS);
                world.getBlockAt(startX + x, startY + y, startZ + width - 1).setType(Material.OAK_PLANKS);
            }
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX, startY + y, startZ + z).setType(Material.OAK_PLANKS);
                world.getBlockAt(startX + length - 1, startY + y, startZ + z).setType(Material.OAK_PLANKS);
            }
        }

        int roofBaseY = startY + wallHeight;
        for (int layer = 0; layer < (width / 2); layer++) {
            int zStart = startZ + layer;
            int zEnd = startZ + width - 1 - layer;
            int y = roofBaseY + layer;
            for (int x = 0; x < length; x++) {
                Block leftStair = world.getBlockAt(startX + x, y, zStart);
                leftStair.setType(Material.OAK_STAIRS);
                Stairs leftData = (Stairs) Bukkit.createBlockData(Material.OAK_STAIRS);
                leftData.setFacing(BlockFace.SOUTH);
                leftData.setHalf(Stairs.Half.TOP);
                leftStair.setBlockData(leftData);

                Block rightStair = world.getBlockAt(startX + x, y, zEnd);
                rightStair.setType(Material.OAK_STAIRS);
                Stairs rightData = (Stairs) Bukkit.createBlockData(Material.OAK_STAIRS);
                rightData.setFacing(BlockFace.NORTH);
                rightData.setHalf(Stairs.Half.TOP);
                rightStair.setBlockData(rightData);
            }
        }

        player.sendMessage("Built a house with a slanted roof!");
        return true;
    }
}

package com.minecraftplay.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;

public class BuildHouseCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /buildhouse <length> <width> [wallMaterial]");
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

        World world = player.getWorld();
        Location base = player.getLocation().getBlock().getLocation();
        int baseY = base.getBlockY();

        int roofY = baseY + 4;
        if (roofY >= world.getMaxHeight()) {
            player.sendMessage("House would exceed world height.");
            return true;
        }

        String wallMaterialArg = args.length >= 3 ? args[2] : null;
        Material wallMat;
        if (wallMaterialArg == null || wallMaterialArg.isBlank()) {
            wallMat = Material.OAK_PLANKS;
        } else {
            wallMat = Material.matchMaterial(wallMaterialArg);
            if (wallMat == null || !wallMat.isBlock()) {
                player.sendMessage("Invalid wall material '" + wallMaterialArg + "'. Using OAK_PLANKS.");
                wallMat = Material.OAK_PLANKS;
            }
        }

        int startX = base.getBlockX();
        int startY = baseY;
        int startZ = base.getBlockZ();

        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                Block b = world.getBlockAt(startX + x, startY - 1, startZ + z);
                b.setType(Material.OAK_PLANKS);
            }
        }

        int wallHeight = 4;
        for (int y = 0; y < wallHeight; y++) {
            for (int x = 0; x < length; x++) {
                Block b1 = world.getBlockAt(startX + x, startY + y, startZ);
                Block b2 = world.getBlockAt(startX + x, startY + y, startZ + width - 1);
                b1.setType(wallMat);
                b2.setType(wallMat);
            }
            for (int z = 0; z < width; z++) {
                Block b1 = world.getBlockAt(startX, startY + y, startZ + z);
                Block b2 = world.getBlockAt(startX + length - 1, startY + y, startZ + z);
                b1.setType(wallMat);
                b2.setType(wallMat);
            }
        }

        int roofLevel = startY + wallHeight;
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX + x, roofLevel, startZ + z).setType(Material.GLASS);
            }
        }

        if (length > 6 && width > 6) {
            furnishHouse(world, startX, startY, startZ, length, width);
        }

        player.sendMessage("Built house " + length + "x" + width + " using " + (wallMaterialArg != null ? wallMaterialArg : "OAK_PLANKS"));
        return true;
    }

    private void furnishHouse(World world, int startX, int startY, int startZ, int length, int width) {
        int insideX = startX + 1;
        int insideZ = startZ + 1;

        if (length >= 6) {
            Location craftLoc = new Location(world, insideX, startY, insideZ);
            craftLoc.getBlock().setType(Material.CRAFTING_TABLE);
            setFacingIfDirectional(craftLoc.getBlock(), BlockFace.SOUTH);

            Location furnaceLoc = new Location(world, insideX + 1, startY, insideZ);
            furnaceLoc.getBlock().setType(Material.FURNACE);
            setFacingIfDirectional(furnaceLoc.getBlock(), BlockFace.SOUTH);

            Location chest1 = new Location(world, insideX + 2, startY, insideZ);
            Location chest2 = new Location(world, insideX + 3, startY, insideZ);
            chest1.getBlock().setType(Material.CHEST);
            chest2.getBlock().setType(Material.CHEST);
            setFacingIfDirectional(chest1.getBlock(), BlockFace.SOUTH);
            setFacingIfDirectional(chest2.getBlock(), BlockFace.SOUTH);
        } else {
            Location craftLoc = new Location(world, insideX, startY, insideZ);
            craftLoc.getBlock().setType(Material.CRAFTING_TABLE);
            setFacingIfDirectional(craftLoc.getBlock(), BlockFace.SOUTH);

            if (insideX + 1 < startX + length - 1) {
                Location furnaceLoc = new Location(world, insideX + 1, startY, insideZ);
                furnaceLoc.getBlock().setType(Material.FURNACE);
                setFacingIfDirectional(furnaceLoc.getBlock(), BlockFace.SOUTH);
            }
            if (insideX + 2 < startX + length - 1) {
                Location chestLoc = new Location(world, insideX + 2, startY, insideZ);
                chestLoc.getBlock().setType(Material.CHEST);
                setFacingIfDirectional(chestLoc.getBlock(), BlockFace.SOUTH);
            }
        }

        int bedZ = startZ + Math.max(1, (width / 2) - 1);
        if (bedZ + 1 >= startZ + width - 1) {
            bedZ = startZ + width - 3;
            if (bedZ < startZ + 1) bedZ = startZ + 1;
        }

        int rightX = startX + length - 2;
        int leftX = startX + 1;

        boolean bedPlaced = false;
        if (rightX - 1 > startX) {
            Location part1 = new Location(world, rightX - 1, startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0);
            if (part2.getBlockX() < startX + length - 1) {
                placeBed(world, part1, part2, BlockFace.EAST);
                bedPlaced = true;
            }
        }

        if (!bedPlaced && leftX + 1 < startX + length - 1) {
            Location part1 = new Location(world, leftX, startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0);
            if (part2.getBlockX() < startX + length - 1) {
                placeBed(world, part1, part2, BlockFace.EAST);
                bedPlaced = true;
            }
        }

        if (!bedPlaced) {
            Location part1 = new Location(world, startX + Math.max(1, length / 2 - 1), startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0);
            placeBed(world, part1, part2, BlockFace.SOUTH);
        }

        int doorX = startX + (length / 2);
        int doorY = startY;
        int doorZ = startZ + (width - 1);

        world.getBlockAt(doorX, doorY, doorZ).setType(Material.AIR);
        world.getBlockAt(doorX, doorY + 1, doorZ).setType(Material.AIR);

        world.getBlockAt(doorX, doorY + 1, doorZ).setType(Material.OAK_DOOR);
        world.getBlockAt(doorX, doorY, doorZ).setType(Material.OAK_DOOR);

        Door upper = (Door) Bukkit.createBlockData(Material.OAK_DOOR);
        upper.setFacing(BlockFace.SOUTH);
        upper.setHalf(Door.Half.TOP);
        world.getBlockAt(doorX, doorY + 1, doorZ).setBlockData(upper);

        Door lower = (Door) Bukkit.createBlockData(Material.OAK_DOOR);
        lower.setFacing(BlockFace.SOUTH);
        lower.setHalf(Door.Half.BOTTOM);
        world.getBlockAt(doorX, doorY, doorZ).setBlockData(lower);
    }

    private void setFacingIfDirectional(Block block, BlockFace face) {
        BlockData data = block.getBlockData();
        if (data instanceof Directional dir) {
            dir.setFacing(face);
            block.setBlockData(dir);
        }
    }

    private void placeBed(World world, Location footLoc, Location headLoc, BlockFace facing) {
        Bed foot = (Bed) Bukkit.createBlockData(Material.WHITE_BED);
        foot.setFacing(facing);
        foot.setPart(Bed.Part.FOOT);
        world.getBlockAt(footLoc).setBlockData(foot);

        Bed head = (Bed) Bukkit.createBlockData(Material.WHITE_BED);
        head.setFacing(facing);
        head.setPart(Bed.Part.HEAD);
        world.getBlockAt(headLoc).setBlockData(head);
    }
}

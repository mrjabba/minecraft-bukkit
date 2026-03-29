package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PlaceRailCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /placerail <length> [railType]");
            return true;
        }
        int length;
        try {
            length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length must be an integer.");
            return true;
        }
        if (length < 1) {
            player.sendMessage("Length must be at least 1.");
            return true;
        }

        Material railType = Material.RAIL;
        if (args.length >= 2) {
            Material maybeType = Material.matchMaterial(args[1]);
            if (maybeType == null ||
                !(maybeType == Material.RAIL ||
                maybeType == Material.POWERED_RAIL ||
                maybeType == Material.DETECTOR_RAIL ||
                maybeType == Material.ACTIVATOR_RAIL)) {
                player.sendMessage("Invalid rail type. Using RAIL.");
            } else {
                railType = maybeType;
            }
        }

        Location start = player.getLocation().getBlock().getLocation();
        World world = player.getWorld();
        BlockFace face = yawToFace(player.getLocation().getYaw());

        for (int i = 0; i < length; i++) {
            Location railLoc = start.clone().add(face.getModX() * i, -1, face.getModZ() * i);
            Block blockBelow = world.getBlockAt(railLoc);
            if (blockBelow.getType().isSolid()) {
                Location railPlace = railLoc.clone().add(0, 1, 0);
                world.getBlockAt(railPlace).setType(railType);
            }
        }
        player.sendMessage("Placed " + length + " " + railType.name() + " in front of you.");
        return true;
    }

    public static BlockFace yawToFace(float yaw) {
        int rot = Math.round(yaw / 90f) & 0x3;
        switch (rot) {
            case 0: return BlockFace.SOUTH;
            case 1: return BlockFace.WEST;
            case 2: return BlockFace.NORTH;
            case 3: return BlockFace.EAST;
            default: return BlockFace.SOUTH;
        }
    }
}

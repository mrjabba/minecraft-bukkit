package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PlaceItemCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /placeitem <length> [itemType]");
            return true;
        }
        int length;
        try {
            length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length must be an integer.");
            return true;
        }

        Material itemType = Material.TORCH;
        if (args.length >= 2) {
            Material maybeType = Material.matchMaterial(args[1]);
            if (maybeType != null && maybeType.isItem()) {
                itemType = maybeType;
            } else {
                player.sendMessage("Invalid item type. Using TORCH.");
            }
        }

        Location start = player.getLocation().getBlock().getLocation();
        World world = player.getWorld();
        BlockFace face = PlaceRailCommand.yawToFace(player.getLocation().getYaw());

        for (int i = 0; i < length; i++) {
            Location itemLoc = start.clone().add(face.getModX() * i, 0, face.getModZ() * i);
            Block blockBelow = world.getBlockAt(itemLoc.clone().add(0, -1, 0));
            if (blockBelow.getType().isSolid()) {
                world.getBlockAt(itemLoc).setType(itemType);
            }
        }
        player.sendMessage("Placed " + length + " " + itemType.name() + " in front of you.");
        return true;
    }
}

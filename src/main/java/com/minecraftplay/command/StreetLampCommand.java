package com.minecraftplay.command;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.Player;

public class StreetLampCommand implements PlayerCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        int height = 4;
        Material armMaterial = Material.GRAY_TERRACOTTA;

        // 1. Parse optional height parameter (args[0])
        if (args.length >= 1) {
            try {
                int parsedHeight = Integer.parseInt(args[0]);
                if (parsedHeight > 0) {
                    height = parsedHeight;
                } else {
                    player.sendMessage("Height must be greater than 0. Using default: 4");
                }
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid height '" + args[0] + "'. Using default: 4");
            }
        }

        // 2. Parse optional material parameter (args[1])
        if (args.length >= 2) {
            Material parsedMat = Material.matchMaterial(args[1]);
            
            // Check if material exists AND is a placeable block (not an item like DIAMOND_SWORD)
            if (parsedMat != null && parsedMat.isBlock()) {
                armMaterial = parsedMat;
            } else {
                player.sendMessage("Invalid block material '" + args[1] + "'. Using default: GRAY_TERRACOTTA");
            }
        }

        // 3. Determine base location 2 blocks ahead of player
        BlockFace facing = player.getFacing();
        Block baseBlock = player.getLocation().getBlock().getRelative(facing, 2);

        // 4. Build pole using the parsed height
        for (int i = 0; i < height; i++) {
            baseBlock.getRelative(BlockFace.UP, i).setType(Material.IRON_BARS);
        }

        // Top of pole is at Y + (height - 1)
        Block topPoleBlock = baseBlock.getRelative(BlockFace.UP, height - 1);

        // 5. Attach the chosen material block facing away from the player
        Block armBlock = topPoleBlock.getRelative(facing);
        armBlock.setType(armMaterial);

        // 6. Hang Iron Chain below arm block
        Block chainBlock = armBlock.getRelative(BlockFace.DOWN);
        chainBlock.setType(Material.CHAIN);

        // 7. Place Lantern below Chain
        Block lanternBlock = chainBlock.getRelative(BlockFace.DOWN);
        lanternBlock.setType(Material.LANTERN);

        // Set the lantern to its hanging state
        if (lanternBlock.getBlockData() instanceof Lantern lanternData) {
            lanternData.setHanging(true);
            lanternBlock.setBlockData(lanternData);
        }

        player.sendMessage("Street lamp constructed! (Height: " + height + ", Material: " + armMaterial.name() + ")");
        return true;
    }
}
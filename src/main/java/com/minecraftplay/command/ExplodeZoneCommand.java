package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

public class ExplodeZoneCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        if (args.length < 1) {
            player.sendMessage("Usage: /explodezone <size>");
            return true;
        }

        int size;
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid size. Must be a number.");
            return true;
        }

        int half = size / 2;
        int y = center.getBlockY();

        for (int x = -half; x <= half; x++) {
            for (int z = -half; z <= half; z++) {
                Location tntLoc = new Location(world, center.getX() + x, y, center.getZ() + z);
                tntLoc.getBlock().setType(Material.TNT);
            }
        }

        int fuseLength = 10;
        for (int i = 1; i <= fuseLength; i++) {
            Location wireLoc = center.clone().add(i, 0, 0);
            wireLoc.getBlock().setType(Material.REDSTONE_WIRE);
        }

        Location leverLoc = center.clone().add(fuseLength + 1, 0, 0);
        leverLoc.getBlock().setType(Material.LEVER);

        BlockState state = leverLoc.getBlock().getState();
        if (state instanceof Switch lever) {
            lever.setFacing(BlockFace.WEST);
            state.setBlockData(lever);
            state.update(true);
        }

        player.sendMessage("Explosive zone created. Flip the lever to detonate.");
        return true;
    }
}

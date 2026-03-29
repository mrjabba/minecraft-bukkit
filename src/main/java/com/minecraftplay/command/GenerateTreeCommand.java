package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GenerateTreeCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /generatetree <treeType>");
            return false;
        }
        if (args.length >= 1) {
            World world = player.getWorld();
            Location loc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(2));
            loc = loc.getBlock().getLocation();

            String treeName = args[0].toUpperCase();
            TreeType treeType;
            try {
                treeType = TreeType.valueOf(treeName);
            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid tree type: " + treeName);
                return true;
            }
            boolean ok = world.generateTree(loc, treeType);
            if (ok) {
                player.sendMessage("Tree generated.");
            } else {
                player.sendMessage("Tree generation failed (not enough space or invalid surface).");
            }
        }

        return true;
    }
}

package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GenerateCherryTreeCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        World world = player.getWorld();
        Location loc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(2));
        loc = loc.getBlock().getLocation();

        boolean ok = world.generateTree(loc, TreeType.CHERRY);
        if (ok) {
            player.sendMessage("Cherry tree generated.");
        } else {
            player.sendMessage("Tree generation failed (not enough space or invalid surface).");
        }
        return true;
    }
}

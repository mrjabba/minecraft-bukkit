package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class WarpCommand implements PlayerCommand {
    private final Map<String, Location> warps;

    public WarpCommand(Map<String, Location> warps) {
        this.warps = warps;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /warp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        Location loc = warps.get(name);
        if (loc == null) {
            player.sendMessage("Warp '" + name + "' does not exist.");
            return true;
        }
        player.teleport(loc);
        player.sendMessage("Warped to '" + name + "'.");
        return true;
    }
}

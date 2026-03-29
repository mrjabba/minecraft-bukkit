package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class ZzzCommand implements PlayerCommand {
    private final Map<String, Location> warps;

    public ZzzCommand(Map<String, Location> warps) {
        this.warps = warps;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        // This is just a silly command I might use to warp out of a dangerous situation
        String name = "bandithome1";
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

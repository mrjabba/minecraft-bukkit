package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Consumer;

public class SetWarpCommand implements PlayerCommand {
    private final Map<String, Location> warps;
    private final Consumer<Void> onWarpSet;

    public SetWarpCommand(Map<String, Location> warps, Consumer<Void> onWarpSet) {
        this.warps = warps;
        this.onWarpSet = onWarpSet;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /setwarp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        warps.put(name, player.getLocation());
        player.sendMessage("Warp '" + name + "' set!");
        onWarpSet.accept(null);
        return true;
    }
}

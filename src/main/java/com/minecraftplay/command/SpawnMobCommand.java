package com.minecraftplay.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        World world = player.getWorld();
        Location center = player.getLocation();

        if (args.length < 3) {
            player.sendMessage("Usage: /spawnmob <mobType> <distance> <amount>");
            return true;
        }

        String mobName = args[0].toUpperCase();
        EntityType type;

        try {
            type = EntityType.valueOf(mobName);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Invalid mob type: " + mobName);
            return true;
        }

        int distance;
        int amount;

        try {
            distance = Integer.parseInt(args[1]);
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid distance or amount. Must be numbers.");
            return true;
        }

        for (int i = 0; i < amount; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double dx = Math.cos(angle) * distance;
            double dz = Math.sin(angle) * distance;

            Location spawnLoc = center.clone().add(dx, 0, dz);
            world.spawnEntity(spawnLoc, type);
        }

        player.sendMessage("Spawned " + amount + " " + mobName.toLowerCase() + "(s) " + distance + " blocks away.");
        return true;
    }
}

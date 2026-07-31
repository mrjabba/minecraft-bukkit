package com.minecraftplay.command;

import com.minecraftplay.TimeOfDay;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetTimeCommand implements PlayerCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /settime <time>");
            return false;
        }

        TimeOfDay timeOfDay = TimeOfDay.fromString(args[0]);

        if (timeOfDay == null) {
            String validOptions = String.join(", ", Arrays.stream(TimeOfDay.values())
                    .map(e -> e.name().toLowerCase())
                    .toList());
            
            player.sendMessage(ChatColor.RED + "Invalid time! Choose from: " + validOptions);
            return true;
        }

        World world = player.getWorld();
        world.setTime(timeOfDay.getTicks());

        player.sendMessage(ChatColor.GREEN + "Set time to " + timeOfDay.name().toLowerCase() + 
                " (" + timeOfDay.getTicks() + " ticks).");
        return true;
    }
}
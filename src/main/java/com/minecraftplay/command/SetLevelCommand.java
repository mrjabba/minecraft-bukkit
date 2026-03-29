package com.minecraftplay.command;

import org.bukkit.entity.Player;

public class SetLevelCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /setlevel <level>");
            return true;
        }
        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid level. Please enter a number.");
            return true;
        }
        if (level < 0) {
            player.sendMessage("Level must be 0 or higher.");
            return true;
        }
        player.setLevel(level);
        player.sendMessage("Your level has been set to " + level + ".");
        return true;
    }
}

package com.minecraftplay.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.minecraftplay.PathManager;
import com.minecraftplay.PathSession;

public class BuildPathCommand implements PlayerCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        // Toggle OFF if already active and no args provided
        if (PathManager.ACTIVE_SESSIONS.containsKey(player.getUniqueId()) && args.length == 0) {
            PathManager.ACTIVE_SESSIONS.remove(player.getUniqueId());
            player.sendMessage(ChatColor.YELLOW + "Path building mode disabled.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /buildpath <width> <style>");
            player.sendMessage(ChatColor.GRAY + "Styles available: dirt, stone, rainbow, nether, yellowbrick");
            return false;
        }

        int width;
        try {
            width = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Width must be a valid number.");
            return false;
        }

        String style = args[1].toLowerCase();
        PathSession session = new PathSession(width, style);
        PathManager.ACTIVE_SESSIONS.put(player.getUniqueId(), session);

        player.sendMessage(ChatColor.GREEN + "Path building enabled! [Width: " + session.getWidth() + ", Style: " + style + "]");
        player.sendMessage(ChatColor.GRAY + "Run '/buildpath' again to disable.");
        return true;
    }
}
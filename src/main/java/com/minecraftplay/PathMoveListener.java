package com.minecraftplay;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PathMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PathSession session = PathManager.ACTIVE_SESSIONS.get(player.getUniqueId());

        if (session == null) return;

        // Ignore rotation-only movement
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        // Must be on the ground
        if (!player.isOnGround()) return;

        Location center = player.getLocation().subtract(0, 1, 0); // Block directly under feet
        int radius = session.getWidth() / 2;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Circle brush shape for natural paths
                if (x * x + z * z > (radius + 0.5) * (radius + 0.5)) continue;

                Block target = center.clone().add(x, 0, z).getBlock();

                // Only convert replaceables like grass, dirt, stone, sand, etc.
                if (isReplaceable(target.getType())) {
                    target.setType(session.getRandomBlock());
                }
            }
        }
    }

    private boolean isReplaceable(Material material) {
        return 
        // TODO need to account for other biomes with these type of material

        material == Material.TERRACOTTA ||
        material == Material.PINK_TERRACOTTA ||
        material == Material.MAGENTA_TERRACOTTA ||
        material == Material.RED_TERRACOTTA ||
        material == Material.ORANGE_TERRACOTTA ||
        material == Material.YELLOW_TERRACOTTA ||
        material == Material.GREEN_TERRACOTTA ||
        material == Material.BLUE_TERRACOTTA ||
        material == Material.PURPLE_TERRACOTTA ||

        material == Material.GRASS_BLOCK ||
        material == Material.DIRT ||
        material == Material.COARSE_DIRT ||
        material == Material.PODZOL ||
        material == Material.MYCELIUM ||
        material == Material.RED_SAND ||
        material == Material.STONE ||
        material == Material.SAND;
    }
}
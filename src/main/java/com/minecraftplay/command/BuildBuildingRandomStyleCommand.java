package com.minecraftplay.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BuildBuildingRandomStyleCommand implements PlayerCommand {

    private static final Map<String, List<Material>> STYLES = new HashMap<>();

    // Populate the supported styles and their corresponding material pools
    static {
        STYLES.put("stone", Arrays.asList(
            Material.SMOOTH_STONE,
            Material.POLISHED_DIORITE,
            Material.POLISHED_GRANITE,
            Material.POLISHED_ANDESITE
        ));

        STYLES.put("terracotta", Arrays.asList(
            Material.WHITE_TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA,
            Material.YELLOW_TERRACOTTA,
            Material.PINK_TERRACOTTA,
            Material.GRAY_TERRACOTTA,
            Material.CYAN_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.BROWN_TERRACOTTA,
            Material.BLUE_TERRACOTTA,
            Material.BLACK_TERRACOTTA
        ));

        STYLES.put("wood", Arrays.asList(
            Material.OAK_PLANKS,
            Material.SPRUCE_PLANKS,
            Material.BIRCH_PLANKS,
            Material.JUNGLE_PLANKS,
            Material.ACACIA_PLANKS,
            Material.CHERRY_PLANKS,
            Material.DARK_OAK_PLANKS,
            Material.MANGROVE_PLANKS,
            Material.BAMBOO_PLANKS
        ));
    }

    @Override
    public boolean execute(Player player, String[] args) {
        System.out.println("STARTING with new building with random style");
        if (args.length < 3) {
            player.sendMessage("Usage: /buildbuildingrandomstyle <radius> <height> <peakedRoof> [style]");
            return false;
        }

        String styleKey;

        // 1. Determine Style Selection
        if (args.length > 3) {
            styleKey = args[3].toLowerCase();
            if (!STYLES.containsKey(styleKey)) {
                player.sendMessage("Unknown style: " + args[3] + ". Valid styles are: " + String.join(", ", STYLES.keySet()));
                return false;
            }
        } else {
            // Pick a random style from the available keys if omitted
            List<String> availableStyles = List.copyOf(STYLES.keySet());
            int randomStyleIndex = ThreadLocalRandom.current().nextInt(availableStyles.size());
            styleKey = availableStyles.get(randomStyleIndex);
        }

        // 2. Select a Random Material from the Chosen Style
        List<Material> materials = STYLES.get(styleKey);
        Material selectedMaterial = materials.get(ThreadLocalRandom.current().nextInt(materials.size()));

        // Optional feedback so the player knows what style/material was chosen
        player.sendMessage("Building with style [" + styleKey + "] using material: " + selectedMaterial.name());

        // 3. Prepare Arguments Array for BuildBuildingCommand
        String[] delegatedArgs = new String[] {
            args[0],               // radius
            args[1],               // height
            args[2],               // peakedRoof
            selectedMaterial.name() // selected random material
        };

        // 4. Re-invoke BuildBuildingCommand
        BuildBuildingCommand command = new BuildBuildingCommand();
        System.out.println("done with new building with random style");

        return command.execute(player, delegatedArgs);
    }
}
package com.minecraftplay;

import org.bukkit.Material;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PathSession {
    private final int width;
    private final String style;
    private final List<Material> palette;

    public PathSession(int width, String style) {
        this.width = Math.min(Math.max(width, 1), 7); // Clamp width between 1 and 7
        this.style = style.toLowerCase();
        this.palette = createPalette(this.style);
    }

    public int getWidth() { return width; }

    public Material getRandomBlock() {
        if (palette.isEmpty()) return Material.DIRT_PATH;
        return palette.get(ThreadLocalRandom.current().nextInt(palette.size()));
    }

    private List<Material> createPalette(String style) {
        List<Material> blocks = new ArrayList<>();
        switch (style) {
            case "dirt":
            default:
                // Weighted list for natural blending
                addMultiple(blocks, Material.DIRT_PATH, 5);
                addMultiple(blocks, Material.COARSE_DIRT, 3);
                addMultiple(blocks, Material.GRAVEL, 2);
                addMultiple(blocks, Material.ROOTED_DIRT, 1);
                break;
            case "stone":
                addMultiple(blocks, Material.COBBLESTONE, 4);
                addMultiple(blocks, Material.MOSSY_COBBLESTONE, 2);
                addMultiple(blocks, Material.STONE, 3);
                addMultiple(blocks, Material.GRAVEL, 1);
                break;
            case "nether":
                addMultiple(blocks, Material.NETHERRACK, 4);
                addMultiple(blocks, Material.BASALT, 3);
                addMultiple(blocks, Material.BLACKSTONE, 2);
                addMultiple(blocks, Material.MAGMA_BLOCK, 1);
                break;
            case "yellowbrick":
                addMultiple(blocks, Material.YELLOW_TERRACOTTA, 1);
                break;
            case "rainbow":
                addMultiple(blocks, Material.PINK_TERRACOTTA, 1);
                addMultiple(blocks, Material.MAGENTA_TERRACOTTA, 1);
                addMultiple(blocks, Material.RED_TERRACOTTA, 1);
                addMultiple(blocks, Material.ORANGE_TERRACOTTA, 1);
                addMultiple(blocks, Material.YELLOW_TERRACOTTA, 1);
                addMultiple(blocks, Material.GREEN_TERRACOTTA, 1);
                addMultiple(blocks, Material.BLUE_TERRACOTTA, 1);
                addMultiple(blocks, Material.PURPLE_TERRACOTTA, 1);
                break;
        }
        return blocks;
    }

    private void addMultiple(List<Material> list, Material mat, int weight) {
        for (int i = 0; i < weight; i++) {
            list.add(mat);
        }
    }
}
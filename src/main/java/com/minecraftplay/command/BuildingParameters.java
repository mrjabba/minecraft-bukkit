package com.minecraftplay.command;

import org.bukkit.Material;

public record BuildingParameters(
    int radius, 
    int height, 
    boolean peakedRoof, 
    Material material
) {
    // 1. Modern Compact Constructor for Validation
    public BuildingParameters {
        if (radius < 8 || height < 6) {
            throw new IllegalArgumentException("Structure too small! Requires minimum radius of 8 and height of 6.");
        }
    }

    // 2. Static Factory to serialize/parse from the raw Bukkit String[] array
    public static BuildingParameters fromArgs(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Usage: /buildbuilding <radius> <height> <peakedRoof> [material]");
        }

        try {
            int radius = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            boolean peakedRoof = Boolean.parseBoolean(args[2]);

            // Handle optional material safely
            Material material = Material.BROWN_TERRACOTTA; // default fallback
            if (args.length > 3) {
                Material matched = Material.matchMaterial(args[3].toUpperCase());
                if (matched == null) {
                    throw new IllegalArgumentException("Unknown Minecraft material: " + args[3]);
                }
                material = matched;
            }

            return new BuildingParameters(radius, height, peakedRoof, material);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid radius or height. Please use whole numbers.");
        }
    }

    // 3. Legacy Adapters: Safely format back to String[] ONLY when interacting with older commands
    public String[] toShellArgs() {
        return new String[] { String.valueOf(radius), String.valueOf(height), material.toString() };
    }

    public String[] toAirFillArgs() {
        return new String[] { String.valueOf(radius - 1), String.valueOf(height - 1), Material.AIR.toString() };
    }
}
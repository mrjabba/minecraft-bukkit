package com.minecraftplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MyPlugin extends JavaPlugin implements Listener {
    private Map<String, Location> warps = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("Kevin plugin enable. 1.2");
        getServer().getPluginManager().registerEvents(this, this);
        loadWarps();
    }

    @Override
    public void onDisable() {
        saveWarps();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String name = cmd.getName().toLowerCase();

        switch (name) {
            case "additem":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return handleAddItem((Player) sender, args);
            case "move":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleMove((Player) sender);
            case "buildmenu":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleBuildMenu((Player) sender);
            case "buildtower":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleBuildTower((Player) sender, args);
            case "buildtowersand":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleBuildTowerSand((Player) sender, args);
            case "generatetree":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleGenerateTree((Player) sender, args);
            case "generatecherrytree":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleGenerateCherryTree((Player) sender);
            case "fillarea":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleFillArea((Player) sender, args);
            case "fillareablock":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleFillAreaBlock((Player) sender, args);
            case "cleararea":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleClearArea((Player) sender, args);
            case "spawnmob":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleSpawnMob((Player) sender, args);
            case "explodezone":
                if (!(sender instanceof Player)) { sender.sendMessage("Only players can run this command."); return true; }
                return handleExplodeZone((Player) sender, args);
            case "buildhouse":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                // pass optional 3rd arg as material name (e.g. ACACIA_PLANKS)
                String wallMatArg = args.length >= 3 ? args[2] : null;
                return handleBuildHouse((Player) sender, args, wallMatArg);
            case "buildhouse2":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return handleBuildHouse2((Player) sender, args);
            case "setlevel":
                 if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can set levels.");
                    return true;
                }
                return handleSetLevel((Player) sender, args);
            case "setwarp":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can set warps.");
                    return true;
                }
                return handleSetWarp((Player) sender, args);
            case "zzz":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can warp.");
                    return true;
                }
                return handlePanic((Player) sender, args);
            case "warp":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can warp.");
                    return true;
                }
                return handleWarp((Player) sender, args);
            case "listwarps":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can list warps.");
                    return true;
                }
                Player p = (Player) sender;
                if (warps.isEmpty()) {
                    p.sendMessage("No warps have been set.");
                } else {
                    p.sendMessage("Warps:");
                    for (String warpName : warps.keySet()) {
                        p.sendMessage("- " + warpName);
                    }
                }
                return true;
            case "placerail":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return handlePlaceRail((Player) sender, args);
            case "placeitem":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return handlePlaceItem((Player) sender, args);
            case "placeflowers":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return handlePlaceFlowers((Player) sender, args);
            case "signgame":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players may run this command.");
                    return true;
                }
                return SignGame.handlePlaceSigns((Player) sender, args);
            default:
                return false;
        }
    }


    private boolean handlePlaceFlowers(Player player, String[] args) {
    if (args.length < 2) {
        player.sendMessage("Usage: /placeflowers <flowerType> <radius>");
        return true;
    }

    String flowerTypeArg = args[0].toUpperCase();
    Material flowerType = Material.matchMaterial(flowerTypeArg);
    if (flowerType == null || !flowerType.isBlock()) {
        player.sendMessage("Invalid flower type: " + args[0]);
        return true;
    }

    int radius;
    try {
        radius = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
        player.sendMessage("Invalid radius: " + args[1]);
        return true;
    }
    if (radius < 1 || radius > 10) {
        player.sendMessage("Radius must be between 1 and 10.");
        return true;
    }

    Location playerLoc = player.getLocation();
    World world = player.getWorld();
    org.bukkit.util.Vector direction = playerLoc.getDirection().normalize();

    // Center point 3 blocks in front of player
    Location center = playerLoc.clone().add(direction.multiply(3));
    center.setY(world.getHighestBlockYAt(center) + 1);

    for (int dx = -radius; dx <= radius; dx++) {
        for (int dz = -radius; dz <= radius; dz++) {
            Location flowerLoc = center.clone().add(dx, 0, dz);
            // Place only if block below is solid and current block is air
            Block below = world.getBlockAt(flowerLoc.clone().add(0, -1, 0));
            Block flowerBlock = world.getBlockAt(flowerLoc);
            if (below.getType().isSolid() && flowerBlock.getType() == Material.AIR) {
                flowerBlock.setType(flowerType);
            }
        }
    }

    player.sendMessage("You placed flowers in front of you!");
    return true;
}
    
    private boolean handleAddItem(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /addItem <itemName> [amount]");
            return true;
        }
        String itemName = args[0].toUpperCase();
        Material material = Material.matchMaterial(itemName);
        if (material == null || !material.isItem()) {
            player.sendMessage("Invalid item name: " + itemName);
            return true;
        }
        int amount = 1;
        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid amount. Using default: 1");
            }
        }
        ItemStack itemStack = new ItemStack(material, amount);
        player.getInventory().addItem(itemStack);
        player.sendMessage("Added " + amount + " " + material.name() + "(s) to your inventory.");
        return true;
    }

    private boolean handleSetLevel(Player player, String[] args) {
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

    private boolean handlePanic(Player player, String[] args) {
        // used when I want to try something but don't want to die and lose stuff
        // String name ="oceanhome";
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

    private boolean handleWarp(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /warp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        Location loc = warps.get(name);
        if (loc == null) {
            player.sendMessage("Warp '" + name + "' does not exist.");
            return true;
        }
        player.teleport(loc);
        player.sendMessage("Warped to '" + name + "'.");
        return true;
    }

    private boolean handleSetWarp(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /setwarp <name>");
            return true;
        }
        String name = args[0].toLowerCase();
        warps.put(name, player.getLocation());
        player.sendMessage("Warp '" + name + "' set!");
        saveWarps();
        return true;
    }

    private void saveWarps() {
        FileConfiguration config = this.getConfig();
        for (Map.Entry<String, Location> entry : warps.entrySet()) {
            config.set("warps." + entry.getKey(), entry.getValue());
        }
        saveConfig();
    }

    private void loadWarps() {
        FileConfiguration config = this.getConfig();
        if (config.isConfigurationSection("warps")) {
            for (String key : config.getConfigurationSection("warps").getKeys(false)) {
                Location loc = config.getLocation("warps." + key);
                if (loc != null) {
                    warps.put(key, loc);
                }
            }
        }
    }

    private boolean handleMove(Player player) {
        Location base = player.getLocation();
        World world = player.getWorld();
        getLogger().info("starting...placing blocks");
        for (int i = 0; i < 5; i++) {
            Location blockLoc = base.clone().add(i, 0, 0); // Shift X by i
            world.getBlockAt(blockLoc).setType(Material.DIRT);
        }
        getLogger().info("starting...done?");
        return true;
    }

    private boolean handleBuildMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, "Build Menu");

        ItemStack dirt = new ItemStack(Material.DIRT);
        ItemMeta dirtMeta = dirt.getItemMeta();
        dirtMeta.setDisplayName("Place Dirt");
        dirt.setItemMeta(dirtMeta);

        menu.setItem(0, dirt);
        player.openInventory(menu);
        return true;
    }

    private boolean handleBuildTower(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /buildtowersand <height> <material>");
            return false;
        }
        if (args.length >= 2) {
            Location base = player.getLocation().add(-2, 0, 0); // Shift X by 2
            World world = player.getWorld();

            Material blockType;
            int height = 10; // Default height
            try {
                height = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid height. Using default: 10");
            }
            String matName = args[1].toUpperCase();
            blockType = Material.matchMaterial(matName);
            if (blockType == null || !blockType.isBlock()) {
                player.sendMessage("Invalid material '" + args[1] + "'. Using STONE.");
                blockType = Material.STONE;
            }

            for (int y = 0; y < height; y++) {
                Location blockLoc = base.clone().add(0, y, 0);
                world.getBlockAt(blockLoc).setType(blockType);
            }
        
        }

        player.sendMessage("Tower built!");
        return true;
    }

    // TODO useful for building visible markers in the ocean
    private boolean handleBuildTowerSand(Player player, String[] args) {
        int height = 10;
        if (args.length == 0) {
            player.sendMessage("Usage: /buildtowersand <height>");
            return false;
        }
        if (args.length >= 1) {
            try {
                height = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }

        Location base = player.getLocation().add(-3, 0, 0);
        World world = player.getWorld();

        Material blockType = Material.SAND; // You can change this to DIRT, GLASS, etc.
        //int height = 10; // Tower height

        for (int y = 0; y < height; y++) {
            Location blockLoc = base.clone().add(0, y, 0);
            world.getBlockAt(blockLoc).setType(blockType);
        }

        player.sendMessage("Sand Tower built!");
        return true;
    }

    private boolean handleGenerateTree(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("Usage: /generatetree <treeType>");
            return false;
        }
        if (args.length >= 1) {
            World world = player.getWorld();
            // pick a spot two blocks ahead of the player's feet and align to block grid
            Location loc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(2));
            loc = loc.getBlock().getLocation();

            // Try generating a tree at the chosen location using the Bukkit generator
            String treeName = args[0].toUpperCase();
            TreeType treeType;
            try {
                treeType = TreeType.valueOf(treeName);
            } catch (IllegalArgumentException e) {
                player.sendMessage("Invalid tree type: " + treeName);
                return true;
            }
            boolean ok = world.generateTree(loc, treeType);
            if (ok) {
                player.sendMessage("Tree generated.");
            } else {
                player.sendMessage("Tree generation failed (not enough space or invalid surface).");
            }
        }

        return true;
    }

    private boolean handleGenerateCherryTree(Player player) {
        World world = player.getWorld();
        // pick a spot two blocks ahead of the player's feet and align to block grid
        Location loc = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(2));
        loc = loc.getBlock().getLocation();

        // Try generating a cherry tree at the chosen location using the Bukkit generator
        boolean ok = world.generateTree(loc, org.bukkit.TreeType.CHERRY);
        if (ok) {
            player.sendMessage("Cherry tree generated.");
        } else {
            player.sendMessage("Tree generation failed (not enough space or invalid surface).");
        }
        return true;
    }

    private boolean handleFillArea(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        int radius = 0; // Default radius
        if (args.length < 2) {
            player.sendMessage("Usage: /fillarea <radius> <material>");
            return false;
        }
        if (args.length >= 2) {
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }
        String matName = args[1].toUpperCase();
        Material blockType = Material.matchMaterial(matName);
        if (blockType == null || !blockType.isBlock()) {
            player.sendMessage("Invalid material '" + args[1] + "'. Using DIRT.");
            blockType = Material.DIRT;
        }

        int yLevel = center.getBlockY();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLoc = new Location(world, center.getX() + x, yLevel, center.getZ() + z);
                world.getBlockAt(blockLoc).setType(blockType);
            }
        }

        player.sendMessage("Filled a " + (radius * 2) + "×" + (radius * 2) + " area with " + blockType.name() + ".");
        return true;
    }

    // Like handleFillArea, but fills a block with specified radius and height
    private boolean handleFillAreaBlock(Player player, String[] args) {
        Location playerLoc = player.getLocation();
        World world = player.getWorld();

        if (args.length < 3) {
            player.sendMessage("Usage: /fillareablock <radius> <height> <material>");
            return false;
        }

        int radius, height;
        try {
            radius = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid radius or height. Using default: 0");
            return false;
        }

        String matName = args[2].toUpperCase();
        Material blockType = Material.matchMaterial(matName);
        if (blockType == null || !blockType.isBlock()) {
            player.sendMessage("Invalid material '" + args[2] + "'. Using DIRT.");
            blockType = Material.DIRT;
        }

        // Calculate the direction the player is facing (block face)
        BlockFace face = yawToFace(playerLoc.getYaw());

        // Find the center of the block to be placed in front of the player
        // Offset by (radius + 1) in the facing direction so the block is in front, not overlapping the player
        int centerX = playerLoc.getBlockX() + face.getModX() * (radius + 1);
        int centerY = playerLoc.getBlockY();
        int centerZ = playerLoc.getBlockZ() + face.getModZ() * (radius + 1);

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    int blockX, blockZ;
                    if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                        blockX = centerX + x;
                        blockZ = centerZ + z;
                    } else {
                        blockX = centerX + z;
                        blockZ = centerZ + x;
                    }
                    Location blockLoc = new Location(world, blockX, centerY + y, blockZ);
                    world.getBlockAt(blockLoc).setType(blockType);
                }
            }
        }

        player.sendMessage("Filled a block of radius " + radius + ", height " + height + " with " + blockType.name() + " in front of you.");
        return true;
    }

    private boolean handleClearArea(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        int radius = 0; // Default radius
        if (args.length == 0) {
            player.sendMessage("Usage: /clearArea <radius>");
            return false;
        }
        if (args.length >= 1) {
            try {
                radius = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid radius. Using default: 0");
            }
        }

        int yLevel = center.getBlockY();
        Material targetBlock = Material.DIRT;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location blockLoc = new Location(world, center.getX() + x, yLevel, center.getZ() + z);
                if (world.getBlockAt(blockLoc).getType() == targetBlock) {
                    world.getBlockAt(blockLoc).setType(Material.AIR);
                }
            }
        }

        player.sendMessage("Cleared dirt in a " + (radius * 2) + "×" + (radius * 2) + " area.");
        return true;
    }

    private boolean handleSpawnMob(Player player, String[] args) {
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

    private boolean handleExplodeZone(Player player, String[] args) {
        Location center = player.getLocation();
        World world = player.getWorld();

        if (args.length < 1) {
            player.sendMessage("Usage: /explodezone <size>");
            return true;
        }

        int size;
        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid size. Must be a number.");
            return true;
        }

        int half = size / 2;
        int y = center.getBlockY();

        // Place TNT blocks in a square
        for (int x = -half; x <= half; x++) {
            for (int z = -half; z <= half; z++) {
                Location tntLoc = new Location(world, center.getX() + x, y, center.getZ() + z);
                tntLoc.getBlock().setType(Material.TNT);
            }
        }

        // Lay redstone fuse in a line from center to lever
        int fuseLength = 10;
        for (int i = 1; i <= fuseLength; i++) {
            Location wireLoc = center.clone().add(i, 0, 0); // +X direction
            wireLoc.getBlock().setType(Material.REDSTONE_WIRE);
        }

        // Place a lever at the end
        Location leverLoc = center.clone().add(fuseLength + 1, 0, 0);
        leverLoc.getBlock().setType(Material.LEVER);

        // Optional: orient the lever correctly
        BlockState state = leverLoc.getBlock().getState();
        if (state instanceof org.bukkit.block.data.type.Switch lever) {
            lever.setFacing(BlockFace.WEST); // Ensure it faces toward the wire
            state.setBlockData(lever);
            state.update(true);
        }

        player.sendMessage("Explosive zone created. Flip the lever to detonate.");
        return true;
    }

    private boolean handleBuildHouse(Player player, String[] args) {
        World world = player.getWorld();
        Location base = player.getLocation().getBlock().getLocation(); // align to block grid

        if (args.length < 2) {
            player.sendMessage("Usage: /buildhouse <length> <width>");
            return false;
        }

        int length;
        int width;
        try {
            length = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length and width must be whole numbers.");
            return true;
        }

        if (length <= 1 || width <= 1) {
            player.sendMessage("Length and width must be greater than 1.");
            return true;
        }

        int wallHeight = 4; // four blocks high

        // Basic safety limits
        int maxDimension = 50; // prevent extremely large builds
        if (length > maxDimension || width > maxDimension) {
            player.sendMessage("Length and width must be <= " + maxDimension + ".");
            return true;
        }

        // Ensure roof won't exceed world max height
        int roofYCheck = base.getBlockY() + wallHeight;
        int worldMaxHeight = world.getMaxHeight();
        if (roofYCheck >= worldMaxHeight) {
            player.sendMessage("Not enough vertical space to build the house here.");
            return true;
        }

        // Build walls: iterate perimeter and build up to wallHeight
        int startX = base.getBlockX();
        int startY = base.getBlockY();
        int startZ = base.getBlockZ();

        Material wallMaterial = Material.OAK_PLANKS;
        Material roofEdge = Material.GLASS;

        // Clear ground area (set floor to oak planks)
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                Location floorLoc = new Location(world, startX + x, startY - 1, startZ + z);
                floorLoc.getBlock().setType(Material.OAK_PLANKS);
            }
        }

        // Walls
        for (int y = 0; y < wallHeight; y++) {
            for (int x = 0; x < length; x++) {
                // front wall (z = 0)
                Location locFront = new Location(world, startX + x, startY + y, startZ + 0);
                locFront.getBlock().setType(wallMaterial);

                // back wall (z = width-1)
                Location locBack = new Location(world, startX + x, startY + y, startZ + (width - 1));
                locBack.getBlock().setType(wallMaterial);
            }
            for (int z = 0; z < width; z++) {
                // left wall (x = 0)
                Location locLeft = new Location(world, startX + 0, startY + y, startZ + z);
                locLeft.getBlock().setType(wallMaterial);

                // right wall (x = length-1)
                Location locRight = new Location(world, startX + (length - 1), startY + y, startZ + z);
                locRight.getBlock().setType(wallMaterial);
            }
        }

        // Roof edge: place glass along the top perimeter at y = startY + wallHeight
        int roofY = startY + wallHeight;
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX + x, roofY, startZ + z).setType(Material.GLASS);
            }
        }

        // Furnish interior if there's enough room.
        if (length > 6 && width > 6) {
            furnishHouse(player, world, startX, startY, startZ, length, width);
        }

        player.sendMessage("Built house " + length + "x" + width + " using " + wallMaterial.name());
        return true;
    }

    private boolean handleBuildHouse(Player player, String[] args, String wallMaterialArg) {
        if (args.length < 2) {
            player.sendMessage("Usage: /buildhouse <length> <width> [wallMaterial]");
            return true;
        }

        int length, width;
        try {
            length = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length and width must be integers.");
            return true;
        }

        if (length <= 1 || width <= 1) {
            player.sendMessage("Length and width must be greater than 1.");
            return true;
        }

        // Resolve wall material string to a Material enum using Bukkit's matching helper.
        Material wallMat;
        if (wallMaterialArg == null || wallMaterialArg.isBlank()) {
            wallMat = Material.OAK_PLANKS;
        } else {
            wallMat = Material.matchMaterial(wallMaterialArg);
            if (wallMat == null || !wallMat.isBlock()) {
                player.sendMessage("Invalid wall material '" + wallMaterialArg + "'. Using OAK_PLANKS.");
                wallMat = Material.OAK_PLANKS;
            }
        }

        Location base = player.getLocation().getBlock().getLocation();
        World world = base.getWorld();
        int baseY = base.getBlockY();

        // safety check on world height
        int roofY = baseY + 4;
        if (roofY >= world.getMaxHeight()) {
            player.sendMessage("House would exceed world height.");
            return true;
        }

        int startX = base.getBlockX();
        int startY = baseY;
        int startZ = base.getBlockZ();

        // floor (place one block below player's current level)
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                Block b = world.getBlockAt(startX + x, startY - 1, startZ + z);
                b.setType(Material.OAK_PLANKS);
            }
        }

        // walls (perimeter, 4 blocks high) using resolved wallMat
        int wallHeight = 4;
        // include ground-level wall blocks (y = 0 .. wallHeight-1)
        for (int y = 0; y < wallHeight; y++) {
            for (int x = 0; x < length; x++) {
                Block b1 = world.getBlockAt(startX + x, startY + y, startZ);
                Block b2 = world.getBlockAt(startX + x, startY + y, startZ + width - 1);
                b1.setType(wallMat);
                b2.setType(wallMat);
            }
            for (int z = 0; z < width; z++) {
                Block b1 = world.getBlockAt(startX, startY + y, startZ + z);
                Block b2 = world.getBlockAt(startX + length - 1, startY + y, startZ + z);
                b1.setType(wallMat);
                b2.setType(wallMat);
            }
        }

        // roof outline with glass
        int roofLevel = startY + wallHeight;
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX + x, roofLevel, startZ + z).setType(Material.GLASS);
            }
        }

        // Furnish interior if there's enough room.
        if (length > 6 && width > 6) {
            furnishHouse(player, world, startX, startY, startZ, length, width);
        }

        player.sendMessage("Built house " + length + "x" + width + " using " + wallMaterialArg);
        return true;
    }

    // Extracted interior furnishing + door placement
    private void furnishHouse(Player player, World world, int startX, int startY, int startZ, int length, int width) {
        int insideX = startX + 1;
        int insideZ = startZ + 1;

        // place crafting + furnace + chest(s) along front interior (z = insideZ)
        if (length >= 6) { // enough horizontal space for crafting, furnace, and a double chest
            Location craftLoc = new Location(world, insideX, startY, insideZ);
            craftLoc.getBlock().setType(Material.CRAFTING_TABLE);
            setFacingIfDirectional(craftLoc.getBlock(), BlockFace.SOUTH);

            Location furnaceLoc = new Location(world, insideX + 1, startY, insideZ);
            furnaceLoc.getBlock().setType(Material.FURNACE);
            setFacingIfDirectional(furnaceLoc.getBlock(), BlockFace.SOUTH);

            Location chest1 = new Location(world, insideX + 2, startY, insideZ);
            Location chest2 = new Location(world, insideX + 3, startY, insideZ);
            chest1.getBlock().setType(Material.CHEST);
            chest2.getBlock().setType(Material.CHEST);
            setFacingIfDirectional(chest1.getBlock(), BlockFace.SOUTH);
            setFacingIfDirectional(chest2.getBlock(), BlockFace.SOUTH);
        } else { // more compact: crafting, furnace, single chest
            Location craftLoc = new Location(world, insideX, startY, insideZ);
            craftLoc.getBlock().setType(Material.CRAFTING_TABLE);
            setFacingIfDirectional(craftLoc.getBlock(), BlockFace.SOUTH);

            if (insideX + 1 < startX + length - 1) {
                Location furnaceLoc = new Location(world, insideX + 1, startY, insideZ);
                furnaceLoc.getBlock().setType(Material.FURNACE);
                setFacingIfDirectional(furnaceLoc.getBlock(), BlockFace.SOUTH);
            }
            if (insideX + 2 < startX + length - 1) {
                Location chestLoc = new Location(world, insideX + 2, startY, insideZ);
                chestLoc.getBlock().setType(Material.CHEST);
                setFacingIfDirectional(chestLoc.getBlock(), BlockFace.SOUTH);
            }
        }

        // place a bed on a side wall (left or right) — not on the back wall where the door will go
        int bedZ = startZ + Math.max(1, (width / 2) - 1); // center-ish along Z inside the house
        if (bedZ + 1 >= startZ + width - 1) {
            bedZ = startZ + width - 3;
            if (bedZ < startZ + 1) bedZ = startZ + 1;
        }

        int rightX = startX + length - 2; // one block inside right wall
        int leftX = startX + 1;           // one block inside left wall

        boolean bedPlaced = false;
        // Try right wall first (preferred) — place bed along X so it sits against the side wall
        if (rightX - 1 > startX) {
            Location part1 = new Location(world, rightX - 1, startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0); // east-west bed
            if (part2.getBlockX() < startX + length - 1) {
                placeBed(world, part1, part2, BlockFace.EAST);
                bedPlaced = true;
            }
        }

        // If right failed, try left wall
        if (!bedPlaced && leftX + 1 < startX + length - 1) {
            Location part1 = new Location(world, leftX, startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0); // east-west bed
            if (part2.getBlockX() < startX + length - 1) {
                placeBed(world, part1, part2, BlockFace.EAST);
                bedPlaced = true;
            }
        }

        // Last-resort: place the bed near the center (should be rare) along X
        if (!bedPlaced) {
            Location part1 = new Location(world, startX + Math.max(1, length / 2 - 1), startY, bedZ);
            Location part2 = part1.clone().add(1, 0, 0);
            placeBed(world, part1, part2, BlockFace.SOUTH);
        }

        // After furnishing, place a wooden door on the wall opposite the crafting/furnace/chest
        int doorX = startX + (length / 2);
        int doorY = startY;
        int doorZ = startZ + (width - 1); // opposite wall from the "front" interior (crafting at startZ+1)

        // Clear space for the door (lower + upper)
        world.getBlockAt(doorX, doorY, doorZ).setType(Material.AIR);
        world.getBlockAt(doorX, doorY + 1, doorZ).setType(Material.AIR);

        // Place door blocks then apply blockdata: set upper half first, then lower half.
        world.getBlockAt(doorX, doorY + 1, doorZ).setType(Material.OAK_DOOR);
        world.getBlockAt(doorX, doorY, doorZ).setType(Material.OAK_DOOR);

        // Upper half
        org.bukkit.block.data.type.Door upper = (org.bukkit.block.data.type.Door) Bukkit.createBlockData(Material.OAK_DOOR);
        upper.setFacing(BlockFace.SOUTH);
        upper.setHalf(org.bukkit.block.data.type.Door.Half.TOP);
        world.getBlockAt(doorX, doorY + 1, doorZ).setBlockData(upper);

        // Lower half
        org.bukkit.block.data.type.Door lower = (org.bukkit.block.data.type.Door) Bukkit.createBlockData(Material.OAK_DOOR);
        lower.setFacing(BlockFace.SOUTH);
        lower.setHalf(org.bukkit.block.data.type.Door.Half.BOTTOM);
        world.getBlockAt(doorX, doorY, doorZ).setBlockData(lower);
    }

    // Helper: set facing for Directional block data when possible
    private void setFacingIfDirectional(Block block, BlockFace face) {
        org.bukkit.block.data.BlockData data = block.getBlockData();
        if (data instanceof org.bukkit.block.data.Directional dir) {
            dir.setFacing(face);
            block.setBlockData(dir);
        }
    }

    // Helper: place a two-block bed with facing and correct HEAD/FOOT parts
    private void placeBed(World world, Location footLoc, Location headLoc, BlockFace facing) {
        // Foot
        org.bukkit.block.data.type.Bed foot = (org.bukkit.block.data.type.Bed) Bukkit.createBlockData(Material.WHITE_BED);
        foot.setFacing(facing);
        foot.setPart(org.bukkit.block.data.type.Bed.Part.FOOT);
        world.getBlockAt(footLoc).setBlockData(foot);

        // Head
        org.bukkit.block.data.type.Bed head = (org.bukkit.block.data.type.Bed) Bukkit.createBlockData(Material.WHITE_BED);
        head.setFacing(facing);
        head.setPart(org.bukkit.block.data.type.Bed.Part.HEAD);
        world.getBlockAt(headLoc).setBlockData(head);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        getLogger().info("starting...onInventoryClick?");    
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
    
        if (!event.getView().getTitle().equals("Build Menu")) return;
    
        event.setCancelled(true); // Prevent item pickup
    
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
    
        if (clicked.getType() == Material.DIRT) {
            Location loc = player.getLocation().add(1, 0, 0); // Place next to player
            loc.getBlock().setType(Material.DIRT);
            player.sendMessage("Placed a dirt block!");
            player.closeInventory();
        }
    }

    private boolean handleBuildHouse2(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Usage: /buildhouse2 <length> <width>");
            return true;
        }
    
        int length, width;
        try {
            length = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length and width must be integers.");
            return true;
        }
    
        if (length <= 1 || width <= 1) {
            player.sendMessage("Length and width must be greater than 1.");
            return true;
        }
    
        Location base = player.getLocation().getBlock().getLocation();
        World world = base.getWorld();
        int startX = base.getBlockX();
        int startY = base.getBlockY();
        int startZ = base.getBlockZ();
    
        // Floor
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX + x, startY - 1, startZ + z).setType(Material.OAK_PLANKS);
            }
        }
    
        // Walls (4 blocks high)
        int wallHeight = 4;
        for (int y = 0; y < wallHeight; y++) {
            for (int x = 0; x < length; x++) {
                world.getBlockAt(startX + x, startY + y, startZ).setType(Material.OAK_PLANKS);
                world.getBlockAt(startX + x, startY + y, startZ + width - 1).setType(Material.OAK_PLANKS);
            }
            for (int z = 0; z < width; z++) {
                world.getBlockAt(startX, startY + y, startZ + z).setType(Material.OAK_PLANKS);
                world.getBlockAt(startX + length - 1, startY + y, startZ + z).setType(Material.OAK_PLANKS);
            }
        }
    
        // Slanted roof using stairs
        int roofBaseY = startY + wallHeight;
        for (int layer = 0; layer < (width / 2); layer++) {
            int zStart = startZ + layer;
            int zEnd = startZ + width - 1 - layer;
            int y = roofBaseY + layer;
            for (int x = 0; x < length; x++) {
                // Left side
                Block leftStair = world.getBlockAt(startX + x, y, zStart);
                leftStair.setType(Material.OAK_STAIRS);
                org.bukkit.block.data.type.Stairs leftData = (org.bukkit.block.data.type.Stairs) Bukkit.createBlockData(Material.OAK_STAIRS);
                leftData.setFacing(BlockFace.SOUTH);
                leftData.setHalf(org.bukkit.block.data.type.Stairs.Half.TOP);
                leftStair.setBlockData(leftData);
    
                // Right side
                Block rightStair = world.getBlockAt(startX + x, y, zEnd);
                rightStair.setType(Material.OAK_STAIRS);
                org.bukkit.block.data.type.Stairs rightData = (org.bukkit.block.data.type.Stairs) Bukkit.createBlockData(Material.OAK_STAIRS);
                rightData.setFacing(BlockFace.NORTH);
                rightData.setHalf(org.bukkit.block.data.type.Stairs.Half.TOP);
                rightStair.setBlockData(rightData);
            }
        }
    
        player.sendMessage("Built a house with a slanted roof!");
        return true;
    }

    private boolean handlePlaceRail(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /placerail <length> [railType]");
            return true;
        }
        int length;
        try {
            length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length must be an integer.");
            return true;
        }
        if (length < 1) {
            player.sendMessage("Length must be at least 1.");
            return true;
        }

        // Default rail type is RAIL, but allow POWERED_RAIL, DETECTOR_RAIL, ACTIVATOR_RAIL
        Material railType = Material.RAIL;
        if (args.length >= 2) {
            Material maybeType = Material.matchMaterial(args[1]);
            if (maybeType == null ||
                !(maybeType == Material.RAIL ||
                maybeType == Material.POWERED_RAIL ||
                maybeType == Material.DETECTOR_RAIL ||
                maybeType == Material.ACTIVATOR_RAIL)) {
                player.sendMessage("Invalid rail type. Using RAIL.");
            } else {
                railType = maybeType;
            }
        }

        Location start = player.getLocation().getBlock().getLocation();
        World world = player.getWorld();
        BlockFace face = yawToFace(player.getLocation().getYaw());

        for (int i = 0; i < length; i++) {
            Location railLoc = start.clone().add(face.getModX() * i, -1, face.getModZ() * i);
            Block blockBelow = world.getBlockAt(railLoc);
            if (blockBelow.getType().isSolid()) {
                Location railPlace = railLoc.clone().add(0, 1, 0);
                world.getBlockAt(railPlace).setType(railType);
            }
        }
        player.sendMessage("Placed " + length + " " + railType.name() + " in front of you.");
        return true;
    }

    private boolean handlePlaceItem(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("Usage: /placeitem <length> [itemType]");
            return true;
        }
        int length;
        try {
            length = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Length must be an integer.");
            return true;
        }
        if (length < 1) {
            player.sendMessage("Length must be at least 1.");
            return true;
        }

        // Default item type is STONE
        Material itemType = Material.STONE;
        if (args.length >= 2) {
            Material maybeType = Material.matchMaterial(args[1]);
            if (maybeType == null) {
                player.sendMessage("Invalid item type. Using STONE.");
            } else {
                itemType = maybeType;
            }
        }

        Location start = player.getLocation().getBlock().getLocation();
        World world = player.getWorld();
        BlockFace face = yawToFace(player.getLocation().getYaw());

        for (int i = 0; i < length; i++) {
            Location railLoc = start.clone().add(face.getModX() * i, -1, face.getModZ() * i);
            Block blockBelow = world.getBlockAt(railLoc);
            if (blockBelow.getType().isSolid()) {
                Location railPlace = railLoc.clone().add(0, 1, 0);
                world.getBlockAt(railPlace).setType(itemType);
            }
        }
        player.sendMessage("Placed " + length + " " + itemType.name() + " in front of you.");
        return true;
    }

    // Helper: convert player yaw to cardinal BlockFace
    private BlockFace yawToFace(float yaw) {
        int rot = Math.round(yaw / 90f) & 0x3;
        switch (rot) {
            case 0: return BlockFace.SOUTH;
            case 1: return BlockFace.WEST;
            case 2: return BlockFace.NORTH;
            case 3: return BlockFace.EAST;
            default: return BlockFace.SOUTH;
        }
    }
}

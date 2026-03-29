package com.minecraftplay;

import com.minecraftplay.command.PlayerCommand;
import com.minecraftplay.command.AddItemCommand;
import com.minecraftplay.command.SetLevelCommand;
import com.minecraftplay.command.WarpCommand;
import com.minecraftplay.command.SetWarpCommand;
import com.minecraftplay.command.BuildTowerCommand;
import com.minecraftplay.command.BuildTowerSandCommand;
import com.minecraftplay.command.FillAreaCommand;
import com.minecraftplay.command.ClearAreaCommand;
import com.minecraftplay.command.GenerateTreeCommand;
import com.minecraftplay.command.SpawnMobCommand;
import com.minecraftplay.command.PlaceFlowersCommand;
import com.minecraftplay.command.GenerateCherryTreeCommand;
import com.minecraftplay.command.PlaceRailCommand;
import com.minecraftplay.command.PlaceItemCommand;
import com.minecraftplay.command.ZzzCommand;
import com.minecraftplay.command.MoveCommand;
import com.minecraftplay.command.BuildMenuCommand;
import com.minecraftplay.command.FillAreaBlockCommand;
import com.minecraftplay.command.ExplodeZoneCommand;
import com.minecraftplay.command.BuildHouseCommand;
import com.minecraftplay.command.BuildHouse2Command;
import com.minecraftplay.command.BuildZigguratCommand;
import com.minecraftplay.command.BuildBridgeCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class MyPlugin extends JavaPlugin implements Listener {
    private Map<String, Location> warps = new HashMap<>();
    
    @FunctionalInterface
    interface PlayerCommandHandler {
        boolean handle(Player player, String[] args);
    }
    
    private final Map<String, PlayerCommandHandler> commands = new HashMap<>();
    
    private void registerCommands() {
        commands.put("additem", new AddItemCommand()::execute);
        commands.put("setlevel", new SetLevelCommand()::execute);
        commands.put("warp", new WarpCommand(warps)::execute);
        commands.put("setwarp", new SetWarpCommand(warps, v -> saveWarps())::execute);
        commands.put("buildtower", new BuildTowerCommand()::execute);
        commands.put("buildtowersand", new BuildTowerSandCommand()::execute);
        commands.put("fillarea", new FillAreaCommand()::execute);
        commands.put("cleararea", new ClearAreaCommand()::execute);
        commands.put("generatetree", new GenerateTreeCommand()::execute);
        commands.put("spawnmob", new SpawnMobCommand()::execute);
        commands.put("placeflowers", new PlaceFlowersCommand()::execute);
        commands.put("generatecherrytree", new GenerateCherryTreeCommand()::execute);
        commands.put("placerail", new PlaceRailCommand()::execute);
        commands.put("placeitem", new PlaceItemCommand()::execute);
        commands.put("zzz", new ZzzCommand(warps)::execute);
        commands.put("move", new MoveCommand(getLogger())::execute);
        commands.put("buildmenu", new BuildMenuCommand()::execute);
        commands.put("fillareablock", new FillAreaBlockCommand()::execute);
        commands.put("explodezone", new ExplodeZoneCommand()::execute);
        commands.put("buildhouse", new BuildHouseCommand()::execute);
        commands.put("buildhouse2", new BuildHouse2Command()::execute);
        commands.put("buildziggurat", new BuildZigguratCommand()::execute);
        commands.put("buildbridge", new BuildBridgeCommand()::execute);
    }

    @Override
    public void onEnable() {
        getLogger().info("Kevin plugin enable. 1.2");
        getServer().getPluginManager().registerEvents(this, this);
        loadWarps();
        registerCommands();
    }

    @Override
    public void onDisable() {
        saveWarps();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String name = cmd.getName().toLowerCase();
        
        if (name.equals("listwarps")) {
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
        }
        
        PlayerCommandHandler handler = commands.get(name);
        if (handler == null) {
            return false;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may run this command.");
            return true;
        }
        
        return handler.handle((Player) sender, args);
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        getLogger().info("starting...onInventoryClick?");    
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
    
        if (!event.getView().getTitle().equals("Build Menu")) return;
    
        event.setCancelled(true);
    
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == org.bukkit.Material.AIR) return;
    
        if (clicked.getType() == org.bukkit.Material.DIRT) {
            Location loc = player.getLocation().add(1, 0, 0);
            loc.getBlock().setType(org.bukkit.Material.DIRT);
            player.sendMessage("Placed a dirt block!");
            player.closeInventory();
        }
    }
}

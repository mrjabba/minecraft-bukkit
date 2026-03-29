package com.minecraftplay.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
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
}

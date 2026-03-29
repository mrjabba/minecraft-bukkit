package com.minecraftplay.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BuildMenuCommand implements PlayerCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        Inventory menu = Bukkit.createInventory(null, 9, "Build Menu");

        ItemStack dirt = new ItemStack(Material.DIRT);
        ItemMeta dirtMeta = dirt.getItemMeta();
        dirtMeta.setDisplayName("Place Dirt");
        dirt.setItemMeta(dirtMeta);

        menu.setItem(0, dirt);
        player.openInventory(menu);
        return true;
    }
}

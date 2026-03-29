package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.inventory.PlayerInventoryMock;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddItemCommandTest {

    private ServerMock server;
    private Player player;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        player = server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new AddItemCommand());
    }

    @Test
    void testAddItemWithValidMaterial() {
        AddItemCommand command = new AddItemCommand();
        String[] args = {"DIAMOND", "5"};
        
        boolean result = command.execute(player, args);
        
        assertTrue(result);
        PlayerInventoryMock inventory = (PlayerInventoryMock) player.getInventory();
        ItemStack[] contents = inventory.getContents();
        boolean found = false;
        for (ItemStack item : contents) {
            if (item != null && item.getType() == Material.DIAMOND && item.getAmount() == 5) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Diamond should be added to inventory");
    }

    @Test
    void testAddItemWithInvalidMaterial() {
        AddItemCommand command = new AddItemCommand();
        String[] args = {"INVALID_MATERIAL"};
        
        boolean result = command.execute(player, args);
        
        assertTrue(result);
    }

    @Test
    void testAddItemWithNoArgs() {
        AddItemCommand command = new AddItemCommand();
        String[] args = {};
        
        boolean result = command.execute(player, args);
        
        assertTrue(result);
    }
}

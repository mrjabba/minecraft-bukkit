package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceItemCommandTest {

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
    void testPlaceItemWithNoArgs() {
        PlaceItemCommand command = new PlaceItemCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceItemWithInvalidLength() {
        PlaceItemCommand command = new PlaceItemCommand();
        String[] args = {"invalid"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceItemWithValidLength() {
        PlaceItemCommand command = new PlaceItemCommand();
        String[] args = {"3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceItemWithCustomItemType() {
        PlaceItemCommand command = new PlaceItemCommand();
        String[] args = {"3", "TORCH"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceItemWithInvalidItemType() {
        PlaceItemCommand command = new PlaceItemCommand();
        String[] args = {"3", "INVALID_ITEM"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

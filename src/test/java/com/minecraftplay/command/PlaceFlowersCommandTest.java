package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceFlowersCommandTest {

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
    void testPlaceFlowersWithValidArgs() {
        PlaceFlowersCommand command = new PlaceFlowersCommand();
        String[] args = {"POPPY", "3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceFlowersWithInvalidFlowerType() {
        PlaceFlowersCommand command = new PlaceFlowersCommand();
        String[] args = {"INVALID_FLOWER", "3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceFlowersWithInvalidRadius() {
        PlaceFlowersCommand command = new PlaceFlowersCommand();
        String[] args = {"POPPY", "abc"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceFlowersWithRadiusOutOfRange() {
        PlaceFlowersCommand command = new PlaceFlowersCommand();
        String[] args = {"POPPY", "15"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceFlowersWithNoArgs() {
        PlaceFlowersCommand command = new PlaceFlowersCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

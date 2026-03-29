package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceRailCommandTest {

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
    void testPlaceRailWithNoArgs() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceRailWithInvalidLength() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {"invalid"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceRailWithZeroLength() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {"0"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceRailWithValidLength() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {"3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceRailWithCustomRailType() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {"3", "POWERED_RAIL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testPlaceRailWithInvalidRailType() {
        PlaceRailCommand command = new PlaceRailCommand();
        String[] args = {"3", "INVALID_RAIL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

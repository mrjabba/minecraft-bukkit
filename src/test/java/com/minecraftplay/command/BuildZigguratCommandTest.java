package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildZigguratCommandTest {

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
    void testBuildZigguratWithNoArgs() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testBuildZigguratWithOnlyLength() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"5"};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testBuildZigguratWithInvalidLength() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"invalid", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildZigguratWithZeroLength() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"0", "STONE"};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testBuildZigguratWithValidArgs() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"5", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildZigguratWithInvalidMaterial() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"5", "INVALID_MATERIAL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildZigguratWithStairs() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"5", "STONE", "stairs"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildZigguratWithStairsTrue() {
        BuildZigguratCommand command = new BuildZigguratCommand();
        String[] args = {"5", "STONE", "true"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

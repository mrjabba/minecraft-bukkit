package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FillAreaCommandTest {

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
    void testFillAreaWithValidArgs() {
        FillAreaCommand command = new FillAreaCommand();
        String[] args = {"3", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testFillAreaWithInvalidRadius() {
        FillAreaCommand command = new FillAreaCommand();
        String[] args = {"abc", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testFillAreaWithInvalidMaterial() {
        FillAreaCommand command = new FillAreaCommand();
        String[] args = {"3", "INVALID_MATERIAL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testFillAreaWithNoArgs() {
        FillAreaCommand command = new FillAreaCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }
}

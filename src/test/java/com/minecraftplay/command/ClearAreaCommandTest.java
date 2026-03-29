package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearAreaCommandTest {

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
    void testClearAreaWithValidRadius() {
        ClearAreaCommand command = new ClearAreaCommand();
        String[] args = {"5"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testClearAreaWithInvalidRadius() {
        ClearAreaCommand command = new ClearAreaCommand();
        String[] args = {"abc"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testClearAreaWithNoArgs() {
        ClearAreaCommand command = new ClearAreaCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }
}

package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetLevelCommandTest {

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
    void testSetLevelWithValidNumber() {
        SetLevelCommand command = new SetLevelCommand();
        String[] args = {"10"};

        boolean result = command.execute(player, args);

        assertTrue(result);
        assertEquals(10, player.getLevel());
    }

    @Test
    void testSetLevelWithInvalidNumber() {
        SetLevelCommand command = new SetLevelCommand();
        String[] args = {"abc"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testSetLevelWithNegativeNumber() {
        SetLevelCommand command = new SetLevelCommand();
        String[] args = {"-5"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testSetLevelWithNoArgs() {
        SetLevelCommand command = new SetLevelCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FillAreaBlockCommandTest {

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
    void testFillAreaBlockWithNoArgs() {
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testFillAreaBlockWithInvalidRadius() {
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        String[] args = {"abc", "5", "STONE"};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testFillAreaBlockWithInvalidMaterial() {
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        String[] args = {"3", "5", "INVALID_MATERIAL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testFillAreaBlockWithValidArgs() {
        FillAreaBlockCommand command = new FillAreaBlockCommand();
        String[] args = {"2", "3", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

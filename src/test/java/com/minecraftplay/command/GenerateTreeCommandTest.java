package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenerateTreeCommandTest {

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
    void testGenerateTreeWithValidType() {
        GenerateTreeCommand command = new GenerateTreeCommand();
        String[] args = {"TREE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testGenerateTreeWithInvalidType() {
        GenerateTreeCommand command = new GenerateTreeCommand();
        String[] args = {"INVALID_TREE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testGenerateTreeWithNoArgs() {
        GenerateTreeCommand command = new GenerateTreeCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }
}

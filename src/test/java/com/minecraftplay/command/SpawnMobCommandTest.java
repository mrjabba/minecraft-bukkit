package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpawnMobCommandTest {

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
    void testSpawnMobWithValidArgs() {
        SpawnMobCommand command = new SpawnMobCommand();
        String[] args = {"ZOMBIE", "5", "3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testSpawnMobWithInvalidMobType() {
        SpawnMobCommand command = new SpawnMobCommand();
        String[] args = {"INVALID_MOB", "5", "3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testSpawnMobWithInvalidNumbers() {
        SpawnMobCommand command = new SpawnMobCommand();
        String[] args = {"ZOMBIE", "abc", "3"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testSpawnMobWithNoArgs() {
        SpawnMobCommand command = new SpawnMobCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildTowerSandCommandTest {

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
    void testBuildTowerSandWithNoArgs() {
        BuildTowerSandCommand command = new BuildTowerSandCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testBuildTowerSandWithValidHeight() {
        BuildTowerSandCommand command = new BuildTowerSandCommand();
        String[] args = {"5"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildTowerSandWithInvalidHeight() {
        BuildTowerSandCommand command = new BuildTowerSandCommand();
        String[] args = {"invalid"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

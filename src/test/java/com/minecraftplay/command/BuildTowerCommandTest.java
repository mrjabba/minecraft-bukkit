package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildTowerCommandTest {

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
    void testBuildTowerWithNoArgs() {
        BuildTowerCommand command = new BuildTowerCommand();
        String[] args = {};

        boolean result = command.execute(player, args);

        assertFalse(result);
    }

    @Test
    void testBuildTowerWithValidArgs() {
        BuildTowerCommand command = new BuildTowerCommand();
        String[] args = {"5", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildTowerWithInvalidHeight() {
        BuildTowerCommand command = new BuildTowerCommand();
        String[] args = {"invalid", "STONE"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildTowerWithInvalidMaterial() {
        BuildTowerCommand command = new BuildTowerCommand();
        String[] args = {"5", "INVALID_MATERIAL"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testBuildTowerWithMissingMaterial() {
        BuildTowerCommand command = new BuildTowerCommand();
        String[] args = {"5"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

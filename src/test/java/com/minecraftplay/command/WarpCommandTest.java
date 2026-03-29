package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WarpCommandTest {

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
    void testWarpToExistingLocation() {
        HashMap<String, Location> warps = new HashMap<>();
        World world = server.addSimpleWorld("test");
        warps.put("spawn", new Location(world, 100, 64, 100));

        WarpCommand command = new WarpCommand(warps);
        String[] args = {"spawn"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testWarpToNonExistentLocation() {
        HashMap<String, Location> warps = new HashMap<>();

        WarpCommand command = new WarpCommand(warps);
        String[] args = {"nonexistent"};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }

    @Test
    void testWarpWithNoArgs() {
        HashMap<String, Location> warps = new HashMap<>();

        WarpCommand command = new WarpCommand(warps);
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

package com.minecraftplay.command;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SetWarpCommandTest {

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
    void testSetWarpWithName() {
        HashMap<String, Location> warps = new HashMap<>();
        Consumer<Void> callback = v -> {};

        SetWarpCommand command = new SetWarpCommand(warps, callback);
        String[] args = {"myspawn"};

        boolean result = command.execute(player, args);

        assertTrue(result);
        assertTrue(warps.containsKey("myspawn"));
    }

    @Test
    void testSetWarpWithNoArgs() {
        HashMap<String, Location> warps = new HashMap<>();
        Consumer<Void> callback = v -> {};

        SetWarpCommand command = new SetWarpCommand(warps, callback);
        String[] args = {};

        boolean result = command.execute(player, args);

        assertTrue(result);
    }
}

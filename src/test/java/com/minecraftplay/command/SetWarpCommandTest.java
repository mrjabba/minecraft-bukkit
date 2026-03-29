package com.minecraftplay.command;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SetWarpCommandTest {

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new SetWarpCommand(new HashMap<>(), v -> {}));
    }
}

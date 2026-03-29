package com.minecraftplay.command;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class WarpCommandTest {

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new WarpCommand(new HashMap<>()));
    }
}

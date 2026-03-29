package com.minecraftplay.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearAreaCommandTest {

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new ClearAreaCommand());
    }
}

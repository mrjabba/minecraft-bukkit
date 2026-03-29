package com.minecraftplay.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetLevelCommandTest {

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new SetLevelCommand());
    }
}

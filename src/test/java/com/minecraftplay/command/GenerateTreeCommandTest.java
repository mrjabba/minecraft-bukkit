package com.minecraftplay.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenerateTreeCommandTest {

    @Test
    void testCommandCanBeCreated() {
        assertNotNull(new GenerateTreeCommand());
    }
}

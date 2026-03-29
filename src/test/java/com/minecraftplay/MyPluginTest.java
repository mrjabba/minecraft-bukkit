package com.minecraftplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyPluginTest {

    @Test
    void testMaterialValidation() {
        var stone = org.bukkit.Material.matchMaterial("STONE");
        assertNotNull(stone);
        
        var invalid = org.bukkit.Material.matchMaterial("NOT_A_MATERIAL");
        assertNull(invalid);
    }

    @Test
    void testIntegerParsing() {
        assertEquals(10, Integer.parseInt("10"));
        assertEquals(0, Integer.parseInt("0"));
        assertThrows(NumberFormatException.class, () -> Integer.parseInt("abc"));
    }
}

package de.norbyte.tubesort.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void construct() {
        assertThrows(IllegalArgumentException.class, () -> new Tile(-1, Color.RED));
        assertThrows(IllegalArgumentException.class, () -> new Tile(0, Color.RED));
        assertThrows(NullPointerException.class, () -> new Tile(1, null));
    }

    @Test
    void canCombine() {
        final Tile tile1 = new Tile(1, Color.RED);
        final Tile tile2 = new Tile(2, Color.RED);
        final Tile tile3 = new Tile(3, Color.DARK_BLUE);

        assertTrue(tile1.canCombine(tile2));
        assertFalse(tile1.canCombine(tile1));
        assertFalse(tile1.canCombine(tile3));
    }

    @Test
    void combine() {
        final Tile tile1 = new Tile(1, Color.RED);
        final Tile tile2 = new Tile(2, Color.RED);
        final Tile tile3 = new Tile(3, Color.DARK_BLUE);

        assertEquals(new Tile(3, Color.RED), tile1.combine(tile2));

        assertThrows(IllegalArgumentException.class, () -> tile2.combine(tile3));
        assertThrows(IllegalArgumentException.class, () -> tile1.combine(tile1));
        assertThrows(IllegalArgumentException.class, () -> tile1.combine(null));
    }
}
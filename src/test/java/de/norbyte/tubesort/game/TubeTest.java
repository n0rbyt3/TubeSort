package de.norbyte.tubesort.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TubeTest {

    @Test
    void construct() {
        assertThrows(NullPointerException.class, () -> new Tube(null));
        assertThrows(NullPointerException.class, () -> new Tube(1, (Tile[]) null));
        assertThrows(NullPointerException.class, () -> new Tube(1, new Tile[]{null}));

        assertThrows(IllegalArgumentException.class, () -> new Tube(-1));
        assertThrows(IllegalArgumentException.class, () -> new Tube(0));
        assertThrows(IllegalArgumentException.class, () -> new Tube(1, new Tile(2, Color.RED)));

        final Tube collapsed = new Tube(2, new Tile(2, Color.RED));
        final Tube expanded = new Tube(2, new Tile(1, Color.RED), new Tile(1, Color.RED));
        assertEquals(collapsed, expanded);
    }

    @Test
    void hash() {
        assertEquals(
                new Tube(2, new Tile(1, Color.RED)).hashCode(),
                new Tube(2, new Tile(1, Color.RED)).hashCode()
        );
    }

    @Test
    void equals() {
        final Tube empty = new Tube(1);
        final Tube filled = new Tube(2, new Tile(1, Color.RED));

        assertEquals(empty, empty);
        assertEquals(empty, new Tube(1));
        assertEquals(filled, new Tube(2, new Tile(1, Color.RED)));

        assertNotEquals(empty, null);
        assertNotEquals(empty, new Tube(2));
        assertNotEquals(filled, new Tube(2, new Tile(1, Color.DARK_BLUE)));
    }

    @Test
    void size() {
        assertEquals(0, new Tube(2).size());
        assertEquals(1, new Tube(2, new Tile(1, Color.RED)).size());
        assertEquals(2, new Tube(2, new Tile(1, Color.RED), new Tile(1, Color.DARK_BLUE)).size());
    }

    @Test
    void getLast() {
        assertNull(new Tube(1).getLast());

        final Tile tile = new Tile(1, Color.RED);
        assertSame(tile, new Tube(3, new Tile(1, Color.DARK_BLUE), tile).getLast());
    }

    @Test
    void isFilled() {
        assertTrue(new Tube(2, new Tile(1, Color.RED)).isFilled());
        assertFalse(new Tube(2).isFilled());
    }

    @Test
    void isComplete() {
        assertTrue(new Tube(1, new Tile(1, Color.RED)).isComplete());
        assertFalse(new Tube(1).isComplete());
        assertFalse(new Tube(2, new Tile(1, Color.RED), new Tile(1, Color.DARK_BLUE)).isComplete());
    }

    @Test
    void canFillInto() {
        final Tube empty = new Tube(2);
        final Tube full = new Tube(3, new Tile(1, Color.RED), new Tile(2, Color.DARK_BLUE));
        final Tube filled = new Tube(2, new Tile(1, Color.RED));
        final Tube complete = new Tube(2, new Tile(2, Color.RED));
        final Tube preComplete = new Tube(2, new Tile(1, Color.RED));

        assertThrows(NullPointerException.class, () -> full.canFillInto(null));

        assertTrue(full.canFillInto(empty));
        assertTrue(filled.canFillInto(preComplete));

        assertFalse(filled.canFillInto(filled)); // self-reference
        assertFalse(complete.canFillInto(empty)); // source is already complete
        assertFalse(filled.canFillInto(empty)); // source and target are switched
        assertFalse(filled.canFillInto(complete)); // target is complete
        assertFalse(full.canFillInto(new Tube(2, new Tile(1, Color.DARK_BLUE)))); // out of space
        assertFalse(full.canFillInto(new Tube(3, new Tile(1, Color.RED)))); // color mismatch
    }

    @Test
    void fill() {
        final Tube empty = new Tube(2);
        final Tube full = new Tube(3, new Tile(1, Color.RED), new Tile(2, Color.DARK_BLUE));
        final Tube filled = new Tube(2, new Tile(1, Color.RED));
        final Tube complete = new Tube(2, new Tile(2, Color.RED));
        final Tube preComplete = new Tube(2, new Tile(1, Color.RED));

        assertThrows(NullPointerException.class, () -> full.fillInto(null));

        final Class<? extends Throwable> iae = IllegalArgumentException.class;
        assertThrows(iae, () -> filled.fillInto(filled)); // self-reference
        assertThrows(iae, () -> complete.fillInto(empty)); // source is already complete
        assertThrows(iae, () -> filled.fillInto(empty)); // source and target are switched
        assertThrows(iae, () -> filled.fillInto(complete)); // target is complete
        assertThrows(iae, () -> full.fillInto(new Tube(2, new Tile(1, Color.DARK_BLUE)))); // out of space
        assertThrows(iae, () -> full.fillInto(new Tube(3, new Tile(1, Color.RED)))); // color mismatch

        final TubePair fullIntoEmpty = new TubePair(
                new Tube(3, new Tile(1, Color.RED)),
                new Tube(2, new Tile(2, Color.DARK_BLUE))
        );

        final TubePair filledIntoPreComplete = new TubePair(
                new Tube(2),
                new Tube(2, new Tile(2, Color.RED))
        );

        assertEquals(fullIntoEmpty, full.fillInto(empty));
        assertEquals(filledIntoPreComplete, filled.fillInto(preComplete));
    }
}

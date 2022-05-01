package de.norbyte.tubesort.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void construct() {
        assertThrows(NullPointerException.class, () -> new Game(2, (Tile[][]) null));
        assertThrows(NullPointerException.class, () -> new Game(2, new Tile[]{null}));
        assertThrows(NullPointerException.class, () -> new Game((Tube[]) null));
        assertThrows(NullPointerException.class, () -> new Game(new Tube[]{null}));
        assertThrows(NullPointerException.class, () -> new Game(null, new TubePair(new Tube(2), new Tube(2))));

        // one blue tile is missing here
        assertThrows(IllegalArgumentException.class, () -> new Game(3,
                new Tile[]{new Tile(1, Color.RED), new Tile(2, Color.DARK_BLUE)},
                new Tile[]{new Tile(2, Color.RED)},
                new Tile[0]
        ));

        // the tile is too large
        assertThrows(IllegalArgumentException.class, () -> new Game(2,
                new Tile[]{new Tile(3, Color.RED)},
                new Tile[]{new Tile(1, Color.DARK_BLUE)},
                new Tile[]{new Tile(1, Color.DARK_BLUE)}
        ));

        final Game expected = new Game(
                new Tube(3, new Tile(1, Color.RED), new Tile(1, Color.DARK_BLUE)),
                new Tube(3, new Tile(2, Color.DARK_BLUE)),
                new Tube(3, new Tile(2, Color.RED))
        );
        final Game actual = new Game(3,
                new Tile[]{new Tile(1, Color.RED), new Tile(1, Color.DARK_BLUE)},
                new Tile[]{new Tile(1, Color.DARK_BLUE), new Tile(1, Color.DARK_BLUE)}, // test collapsing
                new Tile[]{new Tile(2, Color.RED)} // test counting with bigger tiles
        );

        assertEquals(expected, actual);
    }

    @Test
    void move() {
        final Tube p0 = new Tube(2, new Tile(1, Color.RED));
        final Tube p1 = new Tube(2, new Tile(1, Color.RED));
        final Game game = new Game(p0, p1);

        assertThrows(NullPointerException.class, () -> game.move(null, p1));
        assertThrows(NullPointerException.class, () -> game.move(p0, null));
        assertThrows(NullPointerException.class, () -> game.move(null));

        assertThrows(IndexOutOfBoundsException.class, () -> game.move(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> game.move(0, 2));

        final Game expected = new Game(new Tube(2), new Tube(2, new Tile(2, Color.RED)));
        assertEquals(expected, game.move(0, 1));
        assertEquals(expected, game.move(p0, p1));
        assertEquals(expected, game.move(new TubePair(p0, p1)));

        final Game expectedFlipped = new Game(new Tube(2, new Tile(2, Color.RED)), new Tube(2));
        assertEquals(expectedFlipped, game.move(1, 0));
        assertEquals(expectedFlipped, game.move(p1, p0));
        assertEquals(expectedFlipped, game.move(new TubePair(p1, p0)));
    }

    @Test
    void getTubes() {
        final Tube p0 = new Tube(2, new Tile(1, Color.RED));
        final Tube p1 = new Tube(2, new Tile(1, Color.RED));

        assertEquals(List.of(p0, p1), new Game(p0, p1).getTubes());
    }

    @Test
    void isSolved() {
        assertFalse(new Game(
                new Tube(2, new Tile(1, Color.RED)),
                new Tube(2, new Tile(1, Color.RED))
        ).isSolved());

        assertTrue(new Game(
                new Tube(2, new Tile(2, Color.RED)),
                new Tube(2)
        ).isSolved());
    }
}

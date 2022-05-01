package de.norbyte.tubesort.solution;

import de.norbyte.tubesort.game.Color;
import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;
import de.norbyte.tubesort.game.Tube;
import de.norbyte.tubesort.level.Level;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoverTest {

    @Test
    void construct() {
        assertThrows(NullPointerException.class, () -> new Mover((Level) null));
        assertThrows(NullPointerException.class, () -> new Mover((Game) null));
    }

    @Test
    void getPossibleMoves() {
        final Integer tubeSize = 2;

        // test the following rules:
        // - one-direction fill (only 1 on 2 is allowed, but NOT 2 on 1 even if possible)
        // - from complete (0 on 3 is forbidden)
        // - fill onto same single-color tube if available (only 1 on 2, but NOT 1 on 3 even if possible)
        // - fill only on first empty tube
        // in conclusion: only [1 on 2], [3 on 5] and [4 on 5] are allowed
        final Game game2 = new Game(
                new Tube(tubeSize, new Tile(tubeSize, Color.RED)),
                new Tube(tubeSize, new Tile(1, Color.DARK_BLUE)),
                new Tube(tubeSize, new Tile(1, Color.DARK_BLUE)),
                new Tube(tubeSize, new Tile(1, Color.DARK_GREEN), new Tile(1, Color.YELLOW)),
                new Tube(tubeSize, new Tile(1, Color.YELLOW), new Tile(1, Color.DARK_GREEN)),
                new Tube(tubeSize),
                new Tube(tubeSize)
        );

        assertEquals(Set.of(new Step(1, 2), new Step(3, 5), new Step(4, 5)), new Mover(game2)
                .getPossibleMoves()
                .collect(Collectors.toUnmodifiableSet())
        );
    }
}

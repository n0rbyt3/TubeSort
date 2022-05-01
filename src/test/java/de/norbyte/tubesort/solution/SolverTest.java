package de.norbyte.tubesort.solution;

import de.norbyte.tubesort.game.Color;
import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;
import de.norbyte.tubesort.game.Tube;
import de.norbyte.tubesort.level.Level;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SolverTest {

    @Test
    void construct() {
        assertThrows(NullPointerException.class, () -> new Solver((Level) null));
        assertThrows(NullPointerException.class, () -> new Solver((Game) null));
    }

    @Test
    void solve() {
        final Integer tubeSize = 2;
        final Tube p0 = new Tube(tubeSize,
                new Tile(1, Color.RED),
                new Tile(1, Color.DARK_BLUE)
        );
        final Tube p1 = new Tube(tubeSize,
                new Tile(1, Color.DARK_BLUE),
                new Tile(1, Color.RED)
        );
        final Tube p2 = new Tube(tubeSize);
        final Tube p3 = new Tube(tubeSize);
        final Game solvableGame = new Game(p0, p1, p2, p3);

        // there are two ways to solve the game that matches all internal rules:
        // - [@0 into @2], [@1 into @0], [@1 into @2] -> @0=red, @1=empty, @2=blue, @3=empty
        // - [@1 into @2], [@0 into @1], [@0 into @2] -> @0=empty, @1=blue, @2=red, @3=empty
        assertEquals(Set.of(
                List.of(
                        new Step(0, 2),
                        new Step(1, 0),
                        new Step(1, 2)
                ),
                List.of(
                        new Step(1, 2),
                        new Step(0, 1),
                        new Step(0, 2)
                )
        ), new Solver(solvableGame).solve().collect(Collectors.toUnmodifiableSet()));

        // the previous game is not solvable with just 2 moves
        assertEquals(Set.of(), new Solver(solvableGame)
                .solve(2)
                .collect(Collectors.toUnmodifiableSet())
        );

        // this game is not solvable
        assertEquals(Set.of(), new Solver(new Game(p0, p1))
                .solve()
                .collect(Collectors.toUnmodifiableSet())
        );

        // this game is not solvable because a stop is forced after the second move
        assertEquals(Set.of(), new Solver(solvableGame)
                .solve(steps -> steps.size() == 2)
                .collect(Collectors.toUnmodifiableSet())
        );
    }
}

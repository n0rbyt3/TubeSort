package de.norbyte.tubesort.solution;

import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;
import de.norbyte.tubesort.game.Tube;
import de.norbyte.tubesort.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The mover class can calculate all possible moves of a specific game state.
 * It is the core component to solve each game, so its performance is relevant.
 */
public class Mover {

    /**
     * the current game state
     */
    private final Game game;

    /**
     * Creates a new instance for the initial game state of a level
     *
     * @param level the level
     * @throws NullPointerException if level is null
     */
    public Mover(final Level level) {
        this(Objects.requireNonNull(level, "level is null").createGame());
    }

    /**
     * Create a new instance for a specific game state
     *
     * @param game the game state
     * @throws NullPointerException if game is null
     */
    public Mover(final Game game) {
        this.game = Objects.requireNonNull(game, "game is null");
    }

    /**
     * Calculates all possible steps by trying to combine all tubes with themselves
     *
     * @return a stream of possible steps
     */
    public Stream<Step> getPossibleMoves() {
        return getPossibleMoves(null);
    }

    /**
     * Calculates all possible steps with performance optimization by only re-checking changed tube states
     *
     * @param last the last step taken or null to test all combinations
     * @return all possible moves for combinations with modified tubes from the last step
     */
    Stream<Step> getPossibleMoves(final Step last) {
        // rules to increase performance by decreasing the possibilities:
        // 1) do not fill from empty
        // 2) do not fill from complete
        // 3) do only ONE fill to empty
        // 4) do not fill to empty if source is empty afterwards
        // 5) do not fill if fill is split
        // 6) do not fill if colors mismatch
        // 7) do not fill into complete
        // 8) do not fill into itself
        // 9) do only one-direction fill based on position (less position into bigger one)
        // 10) do only fill into a target containing only the same color
        // 11) re-use last possible moves without last move done, recompute only last source to others and others to last step

        final List<Tube> tubes = game.getTubes();
        return IntStream.range(0, tubes.size())
                .filter(fromIndex -> {
                    // rules 1) and 2) for performance reasons
                    final Tube source = tubes.get(fromIndex);
                    return source.isFilled() && !source.isComplete();
                })
                .boxed()
                .parallel()
                .flatMap(fromIndex -> {
                    final Tube source = tubes.get(fromIndex);
                    final AtomicBoolean hadEmpty = new AtomicBoolean(false);
                    final AtomicReference<Tube> optimalTarget = new AtomicReference<>(null);

                    return IntStream.range(0, tubes.size())
                            // rule 8)
                            .filter(toIndex -> fromIndex != toIndex)
                            // rule 11)
                            .filter(toIndex -> last == null || last.contains(fromIndex) || last.contains(toIndex))
                            // rules 1), 2), 4), 5), 6), 7), 8)
                            .filter(toIndex -> {
                                final Tube target = tubes.get(toIndex);
                                return source.canFillInto(target);
                            })
                            // rule 3)
                            .filter(toIndex -> {
                                final Tube target = tubes.get(toIndex);
                                return target.isFilled() || hadEmpty.compareAndSet(false, true);
                            })
                            // rule 9)
                            .filter(toIndex -> {
                                final Tube target = tubes.get(toIndex);
                                final Tile from = source.getLast();
                                final int tileSize = from.size();
                                final boolean fromGetsEmpty = tileSize == source.size();
                                if (fromGetsEmpty && target.size() + tileSize == target.maxSize()) {
                                    return fromIndex < toIndex;
                                }
                                return true;
                            })
                            // rule 10)
                            .filter(toIndex -> {
                                final Tube target = tubes.get(toIndex);
                                final Tile targetLast = target.getLast();
                                final Tube newOptimal = targetLast != null && targetLast.size() == target.size() ? target : null;

                                return optimalTarget.compareAndSet(null, newOptimal);
                            })
                            .mapToObj(toIndex -> new Step(fromIndex, toIndex));
                });
    }

    @Override
    public String toString() {
        return String.format("Mover[game=%s]", game);
    }
}

package de.norbyte.tubesort.solution;

import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * The solver class tries to solve a game by completing all possible steps recursive.
 * It uses parallel streams (threads) for fast calculations. Internally, a <code>Mover</code>
 * instance to calculate possible steps and iterate them recursive
 */
public class Solver {

    /**
     * the current game state
     */
    private final Game game;

    /**
     * all steps taken
     */
    private final List<Step> steps;

    /**
     * a function to allow interrupting the current process
     */
    private Stopper stopper;

    /**
     * Creates a new instance for the initial game state of a level
     *
     * @param level the level
     * @throws NullPointerException if level is null
     */
    public Solver(final Level level) {
        this(Objects.requireNonNull(level, "level is null").createGame());
    }

    /**
     * Create a new instance for a specific game state
     *
     * @param game the game state
     * @throws NullPointerException if game is null
     */
    public Solver(final Game game) {
        this.game = Objects.requireNonNull(game, "game is null");
        this.steps = new ArrayList<>(0);
    }

    /**
     * Creates a copy of the parent state by completing the step
     *
     * @param parent the parent instance
     * @param step   the step to complete
     * @throws NullPointerException      if any parameter is null
     * @throws IndexOutOfBoundsException if the step contains invalid indices
     * @throws IllegalArgumentException  if the step is not allowed
     */
    private Solver(final Solver parent, final Step step) {
        Objects.requireNonNull(parent, "parent is null");
        Objects.requireNonNull(step, "step is null");

        this.game = parent.game.move(step.from(), step.to());

        // copy the step; adding a step MUST NOT manipulate the state of other instances
        this.steps = new ArrayList<>(parent.steps.size() + 1);
        this.steps.addAll(parent.steps);
        this.steps.add(step);

        // re-use the parent stopper
        this.stopper = parent.stopper;
    }

    /**
     * Tries to solve the game with a maximum amount of 15 steps.
     * If the game cannot be completed, an empty stream is returned.
     *
     * @return a stream with an unmodifiable list of unique possible steps to complete the game
     */
    public Stream<List<Step>> solve() {
        return solve(15);
    }

    /**
     * Tries to solve the game within a given amount of steps.
     * If the game cannot be completed, an empty stream is returned.
     *
     * @param maxSteps the maximum amount of steps to complete the game
     * @return a stream with an unmodifiable list of unique possible steps to complete the game
     */
    public Stream<List<Step>> solve(final int maxSteps) {
        return solve(steps -> steps.size() == maxSteps);
    }

    /**
     * Tries to solve the game until no more combinations are available.
     * The stopper can be used to interrupt the process at any time.
     * If the game cannot be completed, an empty stream is returned.
     *
     * @return a stream with an unmodifiable list of unique possible steps to complete the game
     * @throws NullPointerException if stopper is null
     */
    public Stream<List<Step>> solve(final Stopper stopper) {
        Objects.requireNonNull(stopper, "stopper is null");
        this.stopper = stopper;

        return trySolve()
                .map(s -> Collections.unmodifiableList(s.steps))
                .onClose(() -> this.stopper = null);
    }

    @Override
    public String toString() {
        return String.format("Solver[game=%s, steps=%s]", game, steps);
    }

    /**
     * This method performs all possible combinations of the game recursive.
     * Internally it uses a <code>Mover</code> instance to reduce the number of recursive calls significant.
     *
     * @return a stream with all instances which were able to take a step in the game
     */
    private Stream<Solver> trySolve() {
        // abort as early whenever requested
        if (stopper.apply(steps)) {
            return Stream.empty();
        }

        return new Mover(game).getPossibleMoves(getLastStep())
                .map(step -> new Solver(this, step))
                .parallel()
                // replace the current instance with all possible moves to bruteforce into the game
                .flatMap(s -> s.game.isSolved()
                        ? Stream.of(s)
                        : s.trySolve()
                );
    }

    /**
     * Returns the last step taken or <code>null</code> if this is the initial solver
     *
     * @return the last step taken or <code>null</code>
     */
    private Step getLastStep() {
        return steps.isEmpty() ? null : steps.get(steps.size() - 1);
    }
}

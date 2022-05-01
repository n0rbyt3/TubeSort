package de.norbyte.tubesort.game;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A game describes an amount of <code>Tube</code>s in a specific state.
 * The game state is immutable.
 */
public class Game implements Serializable {

    /**
     * a mutable and ordered list of all tubes
     */
    private final List<Tube> tubes;

    /**
     * Creates a new initial game state. This will create new tubes out of the tiles.
     * The size of each tile MUST equal the size of each tube, so all tubes can complete.
     * By adding empty arrays of tiles, empty tubes are added to the game.
     *
     * @param tubeSize the maximum amount of tiles that fit inside a single tube
     * @param tiles    all tiles for each tube
     * @throws NullPointerException     if tiles is <code>null</code> or contains a <code>null</code> value
     * @throws IllegalArgumentException if tiles cannot be used to initialize the game
     */
    public Game(final int tubeSize, final Tile[]... tiles) {
        Objects.requireNonNull(tiles, "tiles is null");

        // all tiles must occur in tube size
        Stream.of(tiles)
                .flatMap(Stream::of)
                .peek(t -> Objects.requireNonNull(t, "tiles must not contain null values"))
                .collect(Collectors.groupingBy(Tile::color, HashMap::new, Collectors.summingInt(Tile::size)))
                .forEach((color, size) -> {
                    if (size != tubeSize) {
                        throw new IllegalArgumentException("invalid occurrences of color " + color);
                    }
                });

        // create tubes
        this.tubes = Stream.of(tiles)
                .map(t -> new Tube(tubeSize, t))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Creates a game in a specific state
     *
     * @param tubes the tubes
     * @throws NullPointerException if tubes is null or contains a <code>null</code> value
     */
    public Game(final Tube... tubes) {
        Objects.requireNonNull(tubes, "tubes is null");

        this.tubes = Arrays.stream(tubes)
                .peek(t -> Objects.requireNonNull(t, "tubes must not contain null values"))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Creates a new game state by performing the single step.
     *
     * @param parent last game state
     * @param step   the step to take
     * @throws NullPointerException if one parameter is null
     */
    public Game(final Game parent, final TubePair step) {
        Objects.requireNonNull(parent, "parent is null");
        Objects.requireNonNull(step, "step is null");

        final TubePair pair = step.first().fillInto(step.second());

        this.tubes = parent.tubes.stream()
                .map(t -> t == step.first() ? pair.first() : t == step.second() ? pair.second() : t)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns an immutable list of tubes in its current state.
     *
     * @return the list of tubes
     */
    public List<Tube> getTubes() {
        return Collections.unmodifiableList(tubes);
    }

    /**
     * Returns true if no incomplete tubes are left over.
     *
     * @return true if the game is solved, false otherwise
     */
    public boolean isSolved() {
        return tubes.stream().allMatch(t -> !t.isFilled() || t.isComplete());
    }

    /**
     * Creates a new game state by filling one tube into another.
     *
     * @param fromIndex the source tube index
     * @param toIndex   the target tube index
     * @throws IndexOutOfBoundsException if any index is &lt; 0 or &gt; number of tubes - 1
     * @see #move(Tube, Tube)
     */
    public Game move(final int fromIndex, final int toIndex) {
        // since toIndex can be less than fromIndex, Objects.checkFromToIndex() cannot be used for validation
        final int length = tubes.size();
        Objects.checkIndex(fromIndex, length);
        Objects.checkIndex(toIndex, length);

        return move(tubes.get(fromIndex), tubes.get(toIndex));
    }

    /**
     * Creates a new game state by filling one tube into another.
     *
     * @param from the source tube
     * @param to   the target tube
     * @throws NullPointerException if any tube is null
     * @see #move(TubePair)
     */
    public Game move(final Tube from, final Tube to) {
        return move(new TubePair(from, to));
    }

    /**
     * Creates a new game state by filling the first tube of the <code>TubePair</code> into the second tube.
     *
     * @param tubes the source and target tubes
     * @throws NullPointerException if tubes is null
     * @see #Game(Game, TubePair)
     */
    public Game move(TubePair tubes) {
        return new Game(this, tubes);
    }

    @Override
    public int hashCode() {
        return tubes.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        return o instanceof Game other && tubes.equals(other.tubes);
    }

    @Override
    public String toString() {
        return String.format("Game[tubes=%s]", tubes);
    }
}

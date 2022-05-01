package de.norbyte.tubesort.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A tube is an immutable container for tiles.
 * It can have three different states: empty (default), filled and complete.
 * A complete tube is filled by only one piece of the tubes maximum size.
 */
public class Tube implements Serializable {

    /**
     * The maximum cumulative size of all tiles
     */
    private final Integer maxSize; // immutable wrapper class to save resources

    /**
     * The cumulative size of all tiles
     */
    private final int size;

    /**
     * An ordered, mutable list of all tiles
     */
    private final List<Tile> tiles;

    /**
     * Creates an empty tube with a specific maximum size
     *
     * @param maxSize the maximum cumulative size of all tiles
     * @throws NullPointerException     if maxSize is null
     * @throws IllegalArgumentException if maxSize is less or equal to zero
     */
    public Tube(final Integer maxSize) {
        this(maxSize, new Tile[0]);
    }

    /**
     * Creates a new tube with a specific maximum size and tiles
     *
     * @param maxSize the maximum cumulative size of all tiles
     * @param tiles   the tiles to hold
     * @throws NullPointerException     if one parameter is null
     * @throws IllegalArgumentException if maxSize is less or equal to zero or the size of all tiles exceeds maxSize
     */
    public Tube(final Integer maxSize, Tile... tiles) {
        Objects.requireNonNull(maxSize, "maxSize is null");
        Objects.requireNonNull(tiles, "tiles is null");

        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }

        int size = 0;
        Tile last = null;
        final List<Tile> tileList = new ArrayList<>(tiles.length);
        for (Tile tile : tiles) {
            if (size + tile.size() > maxSize) {
                throw new IllegalArgumentException("size > maxSize");
            }

            if (last == null || !last.canCombine(tile)) {
                tileList.add(tile);
            } else {
                tileList.set(tileList.size() - 1, last.combine(tile));
            }

            size += tile.size();
            last = tile;
        }

        this.maxSize = maxSize;
        this.size = size;
        this.tiles = tileList;
    }

    /**
     * Creates a modified tube with updated size relative to the modifier.
     * This will create a temporary illegal state of the tube since the size
     * does not match the size of all tiles inside the list.
     *
     * @param original     the tube to modify
     * @param sizeModifier the size modifier
     */
    private Tube(final Tube original, final int sizeModifier) {
        this.maxSize = original.maxSize;
        this.size = original.size + sizeModifier;
        this.tiles = new ArrayList<>(original.tiles);
    }

    /**
     * Returns the maximum cumulative size of all tiles
     *
     * @return maximum cumulative size of all tiles
     */
    public int maxSize() {
        return maxSize;
    }

    /**
     * Returns the size of all tiles in this tube
     *
     * @return cumulative size of all tiles
     */
    public int size() {
        return size;
    }

    /**
     * Returns the last tile or <code>null</code> if this tube is empty
     *
     * @return the last tile or <code>null</code>
     */
    public Tile getLast() {
        return size == 0 ? null : tiles.get(tiles.size() - 1);
    }

    /**
     * Checks whether this tube contains at least one tile
     *
     * @return true if this tube is not empty, false otherwise
     */
    public boolean isFilled() {
        return size > 0;
    }

    /**
     * Checks whether this tube contains only one tile with a size equal to the maximum size of this tube
     *
     * @return true if this tube is complete
     */
    public boolean isComplete() {
        final Tile tile = getLast();
        return tile != null && tile.size() == maxSize;
    }

    /**
     * Checks whether this last tile of this tube can be filled into another tube.
     * The target tube must be empty or its last tile must be in the same color as the last tile of this tube.
     *
     * @param tube the tube to fill in
     * @return true if this tube can be filled onto another tube, false otherwise
     * @throws NullPointerException if the target tube is null
     */
    public boolean canFillInto(final Tube tube) {
        Objects.requireNonNull(tube, "tube is null");

        // cannot fill into itself
        if (tube == this) {
            return false;
        }

        final Tile from = getLast();
        final Tile to = tube.getLast();

        // cannot fill from empty tube
        if (from == null) {
            return false;
        }

        // cannot fill from complete tube
        if (from.size() == maxSize) {
            return false;
        }

        // cannot fill last element into empty tube
        // because it will only exchange the tubes
        if (to == null && from.size() == size) {
            return false;
        }

        // check size
        if (tube.size + from.size() > tube.maxSize) {
            return false;
        }

        // check colors
        return to == null || from.canCombine(to);
    }

    /**
     * Fills the last tile of this tube onto another tube.
     *
     * @param to the tube to fill in
     * @return a pair of tubes containing the new state of this tube and the new state of the other tube
     * @throws NullPointerException     if the target tube is null
     * @throws IllegalArgumentException if this tube is empty or cannot be filled into the target tube
     * @see #canFillInto(Tube)
     */
    public TubePair fillInto(final Tube to) {
        Objects.requireNonNull(to, "to is null");

        if (!canFillInto(to)) {
            throw new IllegalArgumentException(String.format("cannot fill into %s ", to));
        }

        final Tile source = getLast();
        Objects.requireNonNull(source, "source is null");

        final Tile target = to.getLast();

        // from always loses last element
        final int pieceSize = source.size();
        final Tube newFrom = new Tube(this, -pieceSize);
        newFrom.tiles.remove(newFrom.tiles.size() - 1);

        // target tube's last element gets replaced
        final Tube newTo = new Tube(to, pieceSize);
        if (!source.canCombine(target)) {
            newTo.tiles.add(source);
        } else {
            newTo.tiles.set(newTo.tiles.size() - 1, source.combine(target));
        }

        return new TubePair(newFrom, newTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxSize, size, tiles);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        return o instanceof Tube other
                && maxSize.equals(other.maxSize)
                && size == other.size
                && tiles.equals(other.tiles);
    }

    @Override
    public String toString() {
        return String.format("Tube[maxSize=%d, size=%d, stack=%s]", maxSize, size, tiles);
    }
}

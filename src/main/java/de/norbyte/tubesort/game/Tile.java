package de.norbyte.tubesort.game;

import java.io.Serializable;
import java.util.Objects;

/**
 * A tile describes the content of a tube in a specific size and color.
 * It can be combined with another tile if their color matches.
 */
public record Tile(int size, Color color) implements Serializable {

    /**
     * Creates a new tile with size and color.
     *
     * @param size  the tile size
     * @param color the tile color
     * @throws IllegalArgumentException if size < 1
     * @throws NullPointerException     if color is null
     */
    public Tile {
        if (size < 1) {
            throw new IllegalArgumentException("size < 1");
        }
        Objects.requireNonNull(color, "color is null");
    }

    /**
     * Checks whether the tile can be combined with another one.
     * Tiles can only be combined if they have the same color.
     *
     * @param tile the tile to check against
     * @return true if the tiles can be combined, false otherwise
     */
    public boolean canCombine(final Tile tile) {
        return tile != null && tile != this && color == tile.color;
    }

    /**
     * Combines two tiles and returns the combined tile.
     *
     * @param tile the tile to combine
     * @return the combined tile
     * @throws IllegalArgumentException if the tiles cannot be combined
     * @see #canCombine(Tile)
     */
    public Tile combine(final Tile tile) {
        if (!canCombine(tile)) {
            throw new IllegalArgumentException(String.format("the tile cannot be combined with %s", tile));
        }
        return new Tile(size + tile.size, color);
    }

    @Override
    public String toString() {
        return String.format("Tile[size=%d, color=%s]", size, color);
    }
}

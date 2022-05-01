package de.norbyte.tubesort.game;

import java.util.Objects;

/**
 * The class holds a pair of tubes.
 */
public record TubePair(Tube first, Tube second) {

    /**
     * Creates a new pair of tubes
     *
     * @param first  the first tube
     * @param second the second tube
     * @throws NullPointerException     if one tube is null
     * @throws IllegalArgumentException if both tubes are identical
     */
    public TubePair {
        Objects.requireNonNull(first, "first is null");
        Objects.requireNonNull(second, "second is null");

        if (first == second) {
            throw new IllegalArgumentException("first == second");
        }
    }

    @Override
    public String toString() {
        return String.format("TubePair[first=%s, second=%s]", first, second);
    }
}

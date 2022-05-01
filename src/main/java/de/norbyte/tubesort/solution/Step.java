package de.norbyte.tubesort.solution;

/**
 * The step class represents the indices of two tubes which were used to take a step.
 */
public record Step(int from, int to) {

    /**
     * Creates a new instance
     *
     * @param from the source tube index
     * @param to   the target tube index
     * @throws IllegalArgumentException  if both indices are equal
     * @throws IndexOutOfBoundsException if one index is less than zero
     */
    public Step {
        if (from < 0) {
            throw new IndexOutOfBoundsException("from < 0");
        }

        if (to < 0) {
            throw new IndexOutOfBoundsException("to < 0");
        }

        if (from == to) {
            throw new IllegalArgumentException("from == to");
        }
    }

    /**
     * Checks whether the value represents the source tube index or target tube index
     *
     * @param value a number
     * @return true if the source tube index or the target tube index equals the value, false otherwise
     */
    public boolean contains(int value) {
        return from == value || to == value;
    }

    @Override
    public String toString() {
        return String.format("Step[from=%d, to=%d]", from, to);
    }
}

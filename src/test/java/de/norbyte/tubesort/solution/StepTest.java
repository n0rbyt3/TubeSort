package de.norbyte.tubesort.solution;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StepTest {

    @Test
    void construct() {
        assertThrows(IndexOutOfBoundsException.class, () -> new Step(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> new Step(0, -1));

        assertThrows(IllegalArgumentException.class, () -> new Step(0, 0));
    }
}

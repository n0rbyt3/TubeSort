package de.norbyte.tubesort.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TubePairTest {

    @Test
    void construct() {
        final Tube tube = new Tube(2);

        assertThrows(NullPointerException.class, () -> new TubePair(null, tube));
        assertThrows(NullPointerException.class, () -> new TubePair(tube, null));

        assertThrows(IllegalArgumentException.class, () -> new TubePair(tube, tube));
    }
}

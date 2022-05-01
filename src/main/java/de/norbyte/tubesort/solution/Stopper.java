package de.norbyte.tubesort.solution;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface Stopper extends Function<List<Step>, Boolean> {
}

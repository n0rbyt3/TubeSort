package de.norbyte.tubesort;

import de.norbyte.tubesort.level.EasyLevel;
import de.norbyte.tubesort.level.HardLevel;
import de.norbyte.tubesort.level.Level;
import de.norbyte.tubesort.level.RandomLevel;
import de.norbyte.tubesort.solution.Solver;
import de.norbyte.tubesort.solution.Step;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TubeSort {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("usage: TubeSort [<level>[:<steps>],...]");
            System.out.println(" - level: \"easy\", \"hard\" or \"random\"");
            System.out.println(" - steps: maximum number of steps to solve the level, default: 10");
            return;
        }
        Arrays.stream(args).map(TubeSort::parseArgument).forEach(TubeSort::solveAndPrint);
    }

    private static ParsedArgument parseArgument(final String arg) {
        Objects.requireNonNull(arg, "arg is null");

        final String[] nameAndMaxMoves = arg.trim().toLowerCase().split(":", 2);

        final Level level = switch (nameAndMaxMoves[0]) {
            case "easy" -> new EasyLevel();
            case "hard" -> new HardLevel();
            case "random" -> new RandomLevel();
            default -> throw new IllegalArgumentException(String.format("%s is not a valid level name", arg));
        };

        return new ParsedArgument(level, nameAndMaxMoves.length > 1
                ? Integer.parseUnsignedInt(nameAndMaxMoves[1])
                : 10
        );
    }

    private static void solveAndPrint(final ParsedArgument arg) {
        Objects.requireNonNull(arg, "arg is null");

        System.out.printf("-- Solving level \"%s\" --%n", arg.level.getClass().getName());
        final AtomicInteger shortest = new AtomicInteger(Integer.MAX_VALUE);
        final Set<List<Step>> solutions = new Solver(arg.level)
                .solve(arg.maxSteps)
                .filter(steps -> {
                    final int size = steps.size();
                    if (shortest.get() > size) {
                        shortest.set(size);
                    }

                    // continue if shorter or equal
                    return shortest.get() >= size;
                })
                .peek(steps -> System.out.println("Possible solution: " + steps))
                .collect(Collectors.toUnmodifiableSet());

        if (!solutions.isEmpty()) {
            System.out.println();
        }

        solutions
                .stream()
                .filter(steps -> steps.size() == shortest.get())
                .forEach(steps -> System.out.println("Best solution: " + steps));
    }

    private record ParsedArgument(Level level, int maxSteps) {

        public ParsedArgument {
            Objects.requireNonNull(level, "level is null");
        }
    }
}
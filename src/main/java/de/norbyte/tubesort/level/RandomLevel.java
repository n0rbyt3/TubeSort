package de.norbyte.tubesort.level;

import de.norbyte.tubesort.game.Color;
import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A random level which may not be solvable at all
 */
public class RandomLevel implements Level {

    @Override
    public Game createGame() {
        final Random randomizer = new Random();
        final Color[] colors = Color.values();
        final int tubeSize = randomizer.nextInt(colors.length - 2) + 2;

        // create list of required tiles
        final List<Tile> tiles = IntStream.range(0, tubeSize * tubeSize)
                .mapToObj(i -> new Tile(1, colors[i % tubeSize]))
                .collect(Collectors.toCollection(ArrayList::new));

        // randomize tiles
        Collections.shuffle(tiles, randomizer);

        // partition and create array of tiles with two extra empty tubes
        return new Game(tubeSize, IntStream.range(0, tubeSize + 2)
                .mapToObj(i -> i < tubeSize
                        ? tiles.subList(i * tubeSize, i * tubeSize + tubeSize).toArray(Tile[]::new)
                        : new Tile[0])
                .peek(t -> System.out.println(Arrays.toString(t)))
                .toArray(Tile[][]::new)
        );
    }
}

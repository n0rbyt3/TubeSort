package de.norbyte.tubesort.level;

import de.norbyte.tubesort.game.Color;
import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;

/**
 * A complex level which requires some moves to solve
 */
public class HardLevel implements Level {

    @Override
    public Game createGame() {
        return new Game(4,
                new Tile[]{
                        new Tile(1, Color.LIGHT_BLUE),
                        new Tile(1, Color.VIOLET),
                        new Tile(1, Color.LIGHT_BLUE),
                        new Tile(1, Color.GREY),
                },
                new Tile[]{
                        new Tile(1, Color.YELLOW),
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.RED),
                        new Tile(1, Color.GREY),
                },
                new Tile[]{
                        new Tile(2, Color.PINK),
                        new Tile(1, Color.LIGHT_GREEN),
                        new Tile(1, Color.LIGHT_BLUE),
                },
                new Tile[]{
                        new Tile(1, Color.GREY),
                        new Tile(1, Color.ORANGE),
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.RED),
                },
                new Tile[]{
                        new Tile(1, Color.ORANGE),
                        new Tile(1, Color.DARK_GREEN),
                        new Tile(1, Color.LIGHT_BLUE),
                        new Tile(1, Color.YELLOW),
                },
                new Tile[]{
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.RED),
                        new Tile(1, Color.VIOLET),
                        new Tile(1, Color.LIGHT_GREEN),
                },
                new Tile[]{
                        new Tile(1, Color.VIOLET),
                        new Tile(1, Color.GREY),
                        new Tile(1, Color.LIGHT_GREEN),
                        new Tile(1, Color.RED),
                },
                new Tile[]{
                        new Tile(1, Color.LIGHT_GREEN),
                        new Tile(1, Color.VIOLET),
                        new Tile(2, Color.DARK_GREEN),
                },
                new Tile[]{
                        new Tile(2, Color.ORANGE),
                        new Tile(1, Color.PINK),
                        new Tile(1, Color.YELLOW),
                },
                new Tile[]{
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.DARK_GREEN),
                        new Tile(1, Color.PINK),
                        new Tile(1, Color.YELLOW),
                },
                new Tile[0],
                new Tile[0]
        );
    }
}

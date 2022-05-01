package de.norbyte.tubesort.level;

import de.norbyte.tubesort.game.Color;
import de.norbyte.tubesort.game.Game;
import de.norbyte.tubesort.game.Tile;

/**
 * An easy level which can be solved fast
 */
public class EasyLevel implements Level {

    @Override
    public Game createGame() {
        return new Game(4,
                new Tile[]{
                        new Tile(2, Color.ORANGE),
                        new Tile(1, Color.RED),
                        new Tile(1, Color.DARK_BLUE),
                },
                new Tile[]{
                        new Tile(1, Color.ORANGE),
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.RED),
                        new Tile(1, Color.DARK_BLUE),
                },
                new Tile[]{
                        new Tile(1, Color.ORANGE),
                        new Tile(1, Color.RED),
                        new Tile(1, Color.DARK_BLUE),
                        new Tile(1, Color.RED),
                },
                new Tile[0],
                new Tile[0]
        );
    }
}

package fr.proneus.engine.input;

import fr.proneus.engine.Game;

public class MousePosition {

    private Game game;
    private float x, y;

    public MousePosition(Game game, float x, float y) {
        this.game = game;
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public MousePosition toCameraPosition() {
        return new MousePosition(game, x - game.getCamera().getX(), y - game.getCamera().getY());
    }


}

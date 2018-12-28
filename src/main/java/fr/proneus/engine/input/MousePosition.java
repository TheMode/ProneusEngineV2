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

    // TODO toCameraPosition (add Camera#getX and Camera#getY)

    /*public MousePosition toCameraPosition() {
        // Add camera position and zoom to current mouse position
        float mouseX = (x - game.getCamera().getX()) / game.getCamera().getZoomX();
        float mouseY = (y - game.getCamera().getY()) / game.getCamera().getZoomY();
        return new MousePosition(game, x, y);
    }*/


}

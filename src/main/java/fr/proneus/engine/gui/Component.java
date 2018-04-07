package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;

public abstract class Component {

    private boolean visible = true;

    public abstract void update(Game game);

    public abstract void render(Game game, Graphics graphic);

    public void onKeyRepeat(Game game, int key, int scancode) {
    }

    public void onKeyDown(Game game, int key, int scancode) {
    }

    public void onKeyUp(Game game, int key, int scancode) {
    }

    public void onCharCallback(Game game, char character){
    }

    public void onMouseMove(Game game, float x, float y) {
    }

    public void onMouseScroll(Game game, double power) {
    }

    public void onControllerConnect(int joy) {
    }

    public void onControllerDisconnect(int joy) {
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}

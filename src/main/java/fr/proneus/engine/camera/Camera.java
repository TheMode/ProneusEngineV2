package fr.proneus.engine.camera;

import fr.proneus.engine.Game;

public class Camera {

    private Game game;
    private float x, y;
    private float zoomX = 1, zoomY = 1;

    public Camera(Game game) {
        this.game = game;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void setZoom(float x, float y) {
        zoomX = zoomX + x <= 0 ? 0 : x;
        zoomY = zoomY + y <= 0 ? 0 : x;
    }

    public void zoom(float x, float y) {
        zoomX += zoomX + x <= 0 ? 0 : x;
        zoomY += zoomY + y <= 0 ? 0 : x;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZoomX() {
        return zoomX;
    }

    public float getZoomY() {
        return zoomY;
    }

}

package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    protected final float RATIO = Game.getAspectRatio();

    private Matrix4f projection;

    private Vector2f position;

    public Camera() {
        this.position = new Vector2f();
        refreshProjection();
        setPosition(0f, 0f);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    protected Vector2f getPosition() {
        return position;
    }

    public boolean isPartiallyVisible(Sprite sprite) {
        return sprite.interacts(getX(), getY(), 1 + getX(), 1 + getY());
    }

    public boolean isFullyVisible(Sprite sprite) {
        return sprite.fullyInteracts(getX(), getY(), 1 + getX(), 1 + getY());
    }

    protected Matrix4f getProjection() {
        return projection;
    }

    protected void refreshProjection() {
        this.projection = new Matrix4f().ortho(0, RATIO, 1, 0, -1, 1);
    }
}

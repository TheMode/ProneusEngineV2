package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import org.joml.Matrix4f;

public class Camera {

    protected final float RATIO = Game.getAspectRatio();

    private Matrix4f projection;

    public boolean isPartiallyVisible(Sprite sprite) {
        return sprite.interacts(0, 0, 1, 1);
    }

    public boolean isFullyVisible(Sprite sprite) {
        return sprite.fullyInteracts(0, 0, 1, 1);
    }

    protected Matrix4f getProjection() {
        return projection;
    }

    protected void refresh() {
        this.projection = new Matrix4f().ortho(0, RATIO, 1, 0, -1, 1);
    }
}

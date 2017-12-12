package fr.proneus.engine.camera;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Renderable;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;

public class Camera {

    private Game game;
    private float x, y;
    private float zoomX = 1, zoomY = 1;

    private Rectangle cameraRect;

    public Camera(Game game) {
        this.game = game;
        this.cameraRect = new Rectangle(-this.x, -this.y, 1, 1, Color.WHITE, false);
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

    public Rectangle getCameraRectangle() {
        // TODO zoom support
        this.cameraRect.setX(-this.x);
        this.cameraRect.setY(-this.y);
        return cameraRect;
    }

    public boolean isPartiallyVisible(float x, float y, float width, float height) {
        Rectangle camera = getCameraRectangle();
        return camera.interact(x, y) || camera.interact(x + width, y) || camera.interact(x, y + height)
                || camera.interact(x + width, y + height);
    }

    public boolean isPartiallyVisible(Sprite sprite) {
        float width = sprite.getImage().getRegionWidth() * (float) sprite.getImage().getImagePixelWidth() / (float) Game.getDefaultWidth();
        float height = sprite.getImage().getRegionHeight() * (float) sprite.getImage().getImagePixelHeight() / (float) Game.getDefaultHeight();
        return isPartiallyVisible(sprite.getX(), sprite.getY(), width, height);
    }

    public boolean isPartiallyVisible(Renderable renderable) {
        return isPartiallyVisible(renderable.getX(), renderable.getY(), renderable.getWidth(), renderable.getHeight());
    }

    public boolean isFullyVisible(float x, float y, float width, float height) {
        Rectangle camera = getCameraRectangle();
        return camera.interact(x, y) && camera.interact(x + width, y) && camera.interact(x, y + height)
                && camera.interact(x + width, y + height);
    }

    public boolean isFullyVisible(Sprite sprite) {
        float width = sprite.getImage().getRegionWidth() * (float) sprite.getImage().getImagePixelWidth() / (float) Game.getDefaultWidth();
        float height = sprite.getImage().getRegionHeight() * (float) sprite.getImage().getImagePixelHeight() / (float) Game.getDefaultHeight();
        return isFullyVisible(sprite.getX(), sprite.getY(), width, height);
    }

    public boolean isFullyVisible(Renderable renderable) {
        return isFullyVisible(renderable.getX(), renderable.getY(), renderable.getWidth(), renderable.getHeight());
    }

}

package fr.proneus.engine.camera;

import fr.proneus.engine.Game;
import fr.proneus.engine.Game.WindowType;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.graphic.shape.Shape;

public class Camera {

    private Game game;
    private float x, y;
    private float zoomX = 1, zoomY = 1;

    public Camera(Game game) {
        this.game = game;
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
        // TODO faire marcher  les methodes "visible" =(
        // TODO integrate zoom
        //float cameraWidth = !game.getWindowType().equals(WindowType.NORMAL) ? game.getVirtualWidth() : game.getWidth();
        //float cameraHeight = !game.getWindowType().equals(WindowType.NORMAL) ? game.getVirtualHeight() : game.getHeight();
        float cameraWidth = game.getVirtualWidth();
        float cameraHeight = game.getVirtualHeight();
        return new Rectangle(-this.x, -this.y, 1, 1, Color.WHITE, false);
    }

    public boolean isPartiallyVisible(float x, float y, float width, float height) {
        Rectangle camera = getCameraRectangle();
        return camera.interact(x, y) || camera.interact(x + width, y) || camera.interact(x, y + height)
                || camera.interact(x + width, y + height);
    }

    public boolean isPartiallyVisible(Sprite sprite) {
        return isPartiallyVisible(sprite.getX(), sprite.getY(), sprite.getImage().getRegionPixelWidth() / (float) game.getVirtualWidth(),
                sprite.getImage().getRegionPixelHeight() / (float) game.getVirtualHeight());
    }

    public boolean isPartiallyVisible(Shape shape) {
        return isPartiallyVisible(shape.x, shape.y, shape.width, shape.height);
    }

    public boolean isFullyVisible(float x, float y, float width, float height) {
        Rectangle camera = getCameraRectangle();
        return camera.interact(x, y) && camera.interact(x + width, y) && camera.interact(x, y + height)
                && camera.interact(x + width, y + height);
    }

    public boolean isFullyVisible(Sprite sprite) {
        return isFullyVisible(sprite.getX(), sprite.getY(), sprite.getImage().getRegionPixelWidth() / (float) game.getVirtualWidth(),
                sprite.getImage().getRegionPixelHeight() / (float) game.getVirtualHeight());
    }

    public boolean isFullyVisible(Shape shape) {
        return isFullyVisible(shape.x, shape.y, shape.width, shape.height);
    }

}

package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.camera.Camera;

import static org.lwjgl.opengl.GL11.*;

public abstract class Renderable {

    private float x, y, width, height;
    private float regionX, regionY, regionWidth, regionHeight;
    private float z;
    private double scale, scaleX, scaleY;
    private double angle;
    private Color color;
    private DrawType drawType;

    public Renderable(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = 1;
        this.scaleX = 1;
        this.scaleY = 1;
        this.drawType = DrawType.TOP_LEFT;
    }

    public void setImageValue(Image image, float regionX, float regionY, float regionWidth, float regionHeight) {
        this.regionX = regionX * image.getImagePixelWidth();
        this.regionY = regionY * image.getImagePixelHeight();
        this.regionWidth = regionWidth * image.getImagePixelWidth();
        this.regionHeight = regionHeight * image.getImagePixelHeight();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void rotate(double angle) {
        this.angle = angle;
    }

    public void draw(Graphics graphic) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);

        float translateX = 0;
        float translateY = 0;
        switch (drawType) {
            case CENTERED:
                translateX = -getRegionX() - getRegionWidth() / 2;
                translateY = -getRegionY() - getRegionHeight() / 2;
                break;
            case TOP_LEFT:
                translateX = -getRegionX();
                translateY = -getRegionY();
                break;
            case TOP_RIGHT:
                translateX = -getRegionX() + getRegionWidth();
                translateY = -getRegionY();
                break;
            case DOWN_LEFT:
                translateX = -getRegionX();
                translateY = -getRegionY() + getRegionHeight();
                break;
            case DOWN_RIGHT:
                translateX = -getRegionX() + getRegionWidth();
                translateY = -getRegionY() + getRegionHeight();
                break;
        }
        glTranslatef(translateX, translateY, 0);

        if (color != null)
            glColor4f((float) color.r / 255, (float) color.g / 255, (float) color.b / 255, color.a);

        // Sprite position & image region
        float x = this.x * (float) Game.getDefaultWidth();
        float y = this.y * (float) Game.getDefaultHeight();
        glTranslatef(x + regionX + regionWidth / 2, y + regionY + regionHeight / 2, z);

        // Rotation
        if (angle != 0)
            glRotated(angle, 0, 0, 1);

        // Scale (global/sprite)
        double scaleX = Math.max(
                this.scale + (this.scaleX - 1) + (graphic.getGlobalScale() - 1) + (graphic.getScaleX() - 1), 0);
        double scaleY = Math.max(
                this.scale + (this.scaleY - 1) + (graphic.getGlobalScale() - 1) + (graphic.getScaleY() - 1), 0);
        if (scaleX != 1 || scaleY != 1) {
            glScaled(scaleX, scaleY, 1);
        }

        // Scale (camera)
        Camera camera = graphic.getGame().getCamera();
        float cameraZoomX = camera.getZoomX();
        float cameraZoomY = camera.getZoomY();
        glScalef(cameraZoomX, cameraZoomY, 0);

        glTranslatef(-(x + regionX + regionWidth / 2), -(y + regionY + regionHeight / 2), 0);

        render();

        glPopMatrix();
    }

    protected abstract void render();

    public abstract boolean interact(float x, float y);

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getRegionX() {
        return regionX;
    }

    public void setRegionX(float regionX) {
        this.regionX = regionX;
    }

    public float getRegionY() {
        return regionY;
    }

    public void setRegionY(float regionY) {
        this.regionY = regionY;
    }

    public float getRegionWidth() {
        return regionWidth;
    }

    public void setRegionWidth(float regionWidth) {
        this.regionWidth = regionWidth;
    }

    public float getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(float regionHeight) {
        this.regionHeight = regionHeight;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public DrawType getDrawType() {
        return drawType;
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }

    public float getCenterX() {
        return width / 2;
    }

    public float getCenterY() {
        return height / 2;
    }

    public enum DrawType {
        CENTERED, TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }
}

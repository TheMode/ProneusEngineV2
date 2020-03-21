package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.themode.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class Renderable {

    protected final float RATIO = Game.getAspectRatio();

    // OpenGL
    protected float width, height;

    protected Shader shader;

    protected Vector3f position;
    protected float scaleX, scaleY;
    protected float angle;

    protected Origin positionOrigin, rotateOrigin, scaleOrigin;

    protected boolean shouldRefreshModel;

    protected Vector4f color;
    protected float colorIntensity;

    // Matrix
    protected Matrix4f projection;
    protected Matrix4f model;

    protected abstract void render();

    public void draw(Graphics graphics) {
        graphics.addRenderable(this);
    }

    protected void refreshProjectionMatrix(Matrix4f projection) {
        this.projection = projection;
        this.shouldRefreshModel = true;
    }

    protected void refreshModelMatrix() {
        this.model = new Matrix4f();

        float translateOffsetX = width * positionOrigin.getWidthModifier();
        float translateOffsetY = height * positionOrigin.getHeightModifier();
        this.model.translate((position.x + translateOffsetX) * RATIO, position.y + translateOffsetY, position.z);

        this.model.scale(scaleX, scaleY, 0);

        float rotateWidth = rotateOrigin.getWidthModifier() * width;
        float rotateHeight = rotateOrigin.getHeightModifier() * height;
        this.model.translate(rotateWidth, rotateHeight, 0);
        this.model.rotate((float) Math.toRadians(angle), new Vector3f(0, 0, 1));
        this.model.translate(-rotateWidth, -rotateHeight, 0);
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.shouldRefreshModel = true;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3f(x, y, position.z);
        this.shouldRefreshModel = true;
    }

    public void move(float x, float y) {
        setPosition(getX() + x, getY() + y);
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        this.position.x = x;
        this.shouldRefreshModel = true;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        this.position.y = y;
        this.shouldRefreshModel = true;
    }

    public float getZ() {
        return position.z;
    }

    public void setZ(float z) {
        this.position.z = z;
        this.shouldRefreshModel = true;
    }

    public void scale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.shouldRefreshModel = true;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        this.shouldRefreshModel = true;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        this.shouldRefreshModel = true;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.shouldRefreshModel = true;
    }

    public void setPositionOrigin(Origin positionOrigin) {
        this.positionOrigin = positionOrigin;
        this.shouldRefreshModel = true;
    }

    public void setRotateOrigin(Origin rotateOrigin) {
        this.rotateOrigin = rotateOrigin;
        this.shouldRefreshModel = true;
    }

    public void setScaleOrigin(Origin scaleOrigin) {
        this.scaleOrigin = scaleOrigin;
        this.shouldRefreshModel = true;
    }

    public void setColor(Color color, float colorIntensity) {
        this.color = new Vector4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        setColorIntensity(colorIntensity);
    }

    public void setColor(Color color) {
        setColor(color, 1f);
    }

    public void setColorIntensity(float colorIntensity) {
        this.colorIntensity = MathUtils.minMax(colorIntensity, 0f, 1f);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Shader getShader() {
        return shader;
    }
}

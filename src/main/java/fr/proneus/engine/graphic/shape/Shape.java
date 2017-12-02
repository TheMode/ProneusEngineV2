package fr.proneus.engine.graphic.shape;

import static org.lwjgl.opengl.GL11.*;

import fr.proneus.engine.camera.Camera;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;

public abstract class Shape {

    public float x, y, width, height;
    private boolean filled;
    private int red, green, blue;
    private float alpha;
    private double angle;

    public Shape(float x, float y, float width, float height, Color color, boolean filled) {
        this.x = x * 1920f;
        this.y = y * 1080f;
        this.width = width * 1920f;
        this.height = height * 1080f;
        setColor(color);
        this.filled = filled;
    }

    public Shape(float x, float y, float width, float height) {
        this(x, y, width, height, Color.WHITE, true);
    }

    public void setColor(Color color) {
        this.red = color.r;
        this.green = color.g;
        this.blue = color.b;
        this.alpha = color.a;
    }

    public void rotate(double angle) {
        this.angle = angle;
    }

    public void draw(Graphics graphic) {
        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, 0);

        glTranslatef(x, y, 1);

        // Rotation
        if (angle != 0)
            glRotated(angle, 0, 0, 1);

        // Scale (camera)
        Camera camera = graphic.getGame().getCamera();
        float x = camera.getZoomX();
        float y = camera.getZoomY();
        glScalef(x, y, 0);

        glColor4f((float) red / 255, (float) green / 255, (float) blue / 255, alpha);

        render();

        glColor4f(1, 1, 1, 1);

        glPopMatrix();
    }

    public boolean isFilled() {
        return filled;
    }

    protected abstract void render();

    public abstract boolean interact(float x, float y);

}

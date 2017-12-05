package fr.proneus.engine.graphic.shape;

import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Renderable;

import static org.lwjgl.opengl.GL11.*;

public class Circle extends Renderable {

    private float radius;
    private boolean filled;

    public Circle(float x, float y, Color color, float radius, boolean filled) {
        super(x, y, 0, 0);
        setColor(color);
        this.radius = radius;
        this.filled = filled;
    }

    public Circle(float x, float y, float radius) {
        this(x, y, Color.WHITE, radius, true);
    }

    @Override
    protected void render() {

        if (filled) {

            glShadeModel(GL_SMOOTH);
            glBegin(GL_TRIANGLE_FAN);
            float y1 = getY();
            float x1 = getX();

            for (int i = 0; i <= 360; i++) {
                float degInRad = (float) Math.toRadians(i);
                float x2 = getX() + ((float) Math.cos(degInRad) * radius);
                float y2 = getY() + ((float) Math.sin(degInRad) * radius);
                glVertex2f(getX(), getY());
                glVertex2f(x1 + getX(), y1 + getY());
                glVertex2f(x2 + getX(), y2 + getY());
                y1 = y2;
                x1 = x2;
            }
            glEnd();

        } else {
            glBegin(GL_LINE_LOOP);

            for (int i = 0; i <= 360; i++) {
                float degInRad = (float) Math.toRadians(i);
                glVertex2f(getX() + ((float) Math.cos(degInRad) * radius), getY() + ((float) Math.sin(degInRad) * radius));
            }

            glEnd();
        }

    }

    @Override
    public boolean interact(float x, float y) {
        double distance = Math.sqrt(Math.pow((this.getX() - x), 2) + Math.pow(this.getY() - y, 2));
        return Math.abs(distance) < radius;
    }

}

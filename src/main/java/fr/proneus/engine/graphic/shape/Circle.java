package fr.proneus.engine.graphic.shape;

import fr.proneus.engine.graphic.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Circle extends Shape {

    private float radius;

    public Circle(float x, float y, Color color, float radius, boolean filled) {
        super(x, y, 0, 0, color, filled);
        this.radius = radius;
    }

    public Circle(float x, float y, float radius) {
        this(x, y, Color.WHITE, radius, true);
    }

    @Override
    protected void render() {

        if (isFilled()) {

            glShadeModel(GL_SMOOTH);
            glBegin(GL_TRIANGLE_FAN);
            float y1 = y;
            float x1 = x;

            for (int i = 0; i <= 360; i++) {
                float degInRad = (float) Math.toRadians(i);
                float x2 = x + ((float) Math.cos(degInRad) * radius);
                float y2 = y + ((float) Math.sin(degInRad) * radius);
                glVertex2f(x, y);
                glVertex2f(x1, y1);
                glVertex2f(x2, y2);
                y1 = y2;
                x1 = x2;
            }
            glEnd();

        } else {
            glBegin(GL_LINE_LOOP);

            for (int i = 0; i <= 360; i++) {
                float degInRad = (float) Math.toRadians(i);
                glVertex2f(x + ((float) Math.cos(degInRad) * radius), y + ((float) Math.sin(degInRad) * radius));
            }

            glEnd();
        }

    }

    @Override
    public boolean interact(float x, float y) {
        double distance = Math.sqrt(Math.pow((this.x - x), 2) + Math.pow(this.y - y, 2));
        return Math.abs(distance) < radius;
    }

}

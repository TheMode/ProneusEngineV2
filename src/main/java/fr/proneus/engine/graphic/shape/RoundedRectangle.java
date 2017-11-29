package fr.proneus.engine.graphic.shape;

import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.shape.Shape;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class RoundedRectangle extends Shape {

    private float angle = 20f;

    public RoundedRectangle(float x, float y, float width, float height, Color color, boolean filled) {
        super(x, y, width, height, color, filled);
    }

    public static void DrawFilledCircle(float x, float y, float radius) {
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
    }

    public static void DrawCircle(float x, float y, float radius) {
        glBegin(GL_LINE_LOOP);

        for (int i = 0; i <= 360; i++) {
            float degInRad = (float) Math.toRadians(i);
            glVertex2f(x + ((float) Math.cos(degInRad) * radius), y + ((float) Math.sin(degInRad) * radius));
        }

        glEnd();
    }

    @Override
    protected void render() {

        glBegin(GL_QUADS);

        glVertex2f(x + angle, y);
        glVertex2f(x + width - angle, y);
        glVertex2f(x + width - angle, y + height);
        glVertex2f(x + angle, y + height);

        glEnd();

        glBegin(GL_QUADS);

        glVertex2f(x, y + angle);
        glVertex2f(x + width, y + angle);
        glVertex2f(x + width, y + height - angle);
        glVertex2f(x, y + height - angle);

        glEnd();

        DrawFilledCircle(x + angle * 0.97f, y + angle * 0.97f, angle);
        DrawFilledCircle(x + width - angle * 0.97f, y + angle * 0.97f, angle);
        DrawFilledCircle(x + angle * 0.97f, y + height - angle * 0.97f, angle);
        DrawFilledCircle(x + width - angle * 0.97f, y + height - angle * 0.97f, angle);

        // OUTLINE
        glBegin(GL_LINE_LOOP);

        glVertex2f(x, y + angle);
        glVertex2f(x, y + height - angle);

        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex2f(x + width - angle / 10, y + angle);
        glVertex2f(x + width - angle / 10, y + height - angle);

        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex2f(x + angle, y + angle / 10);
        glVertex2f(x + width - angle, y + angle / 10);

        glEnd();

        glBegin(GL_LINE_LOOP);

        glVertex2f(x + angle, y + height);
        glVertex2f(x + width - angle, y + height);

        glEnd();

        DrawCircle(x + angle, y + angle, angle);
        DrawCircle(x + width - angle, y + angle, angle);
        DrawCircle(x + angle, y + height - angle, angle);
        DrawCircle(x + width - angle, y + height - angle, angle);

        DrawFilledCircle(x + angle * 1.1f, y + angle * 1.1f, angle);
        DrawFilledCircle(x + width - angle * 1.1f, y + angle * 1.1f, angle);
        DrawFilledCircle(x + angle * 1.1f, y + height - angle * 1.1f, angle);
        DrawFilledCircle(x + width - angle * 1.1f, y + height - angle * 1.1f, angle);

        DrawFilledCircle(x + angle * 1.1f, y + angle * 1.3f, angle);
        DrawFilledCircle(x + width - angle * 1.1f, y + angle * 1.3f, angle);
        DrawFilledCircle(x + angle * 1.1f, y + height - angle * 1.3f, angle);
        DrawFilledCircle(x + width - angle * 1.1f, y + height - angle * 1.3f, angle);

        DrawFilledCircle(x + angle * 1.3f, y + angle * 1.1f, angle);
        DrawFilledCircle(x + width - angle * 1.3f, y + angle * 1.1f, angle);
        DrawFilledCircle(x + angle * 1.3f, y + height - angle * 1.1f, angle);
        DrawFilledCircle(x + width - angle * 1.3f, y + height - angle * 1.1f, angle);
    }

    @Override
    public boolean interact(float x, float y) {
        return false;
    }
}

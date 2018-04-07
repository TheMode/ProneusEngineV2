package fr.proneus.engine.graphic.shape;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Renderable;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Renderable {

    private boolean filled;

    public Rectangle(float x, float y, float width, float height, Color color, boolean filled) {
        super(x, y, width, height);
        setColor(color);
        this.filled = filled;
    }

    public Rectangle(float x, float y, float width, float height) {
        this(x, y, width, height, Color.WHITE, true);
    }

    @Override
    public void render() {
        float x = getX() * (float) Game.getCameraWidth();
        float y = getY() * (float) Game.getCameraHeight();
        float width = getWidth() * (float) Game.getCameraWidth();
        float height = getHeight() * (float) Game.getCameraHeight();

        glBegin(filled ? GL_QUADS : GL_LINE_LOOP);

        glVertex2f(x, y);
        glVertex2f(x, height + y);
        glVertex2f(width + x, height + y);
        glVertex2f(width + x, y);

        glEnd();
    }

    @Override
    public boolean interact(float x, float y) {
        return getX() <= x && getX() + getWidth() >= x && getY() <= y && getY() + getHeight() >= y;
    }

    public boolean overlaps (Rectangle r) {
        return getX() < r.getX() + r.getWidth() && getX() + getWidth() > r.getX() && getY() < r.getY() + r.getHeight() && getY() + getHeight() > r.getY();
    }

}

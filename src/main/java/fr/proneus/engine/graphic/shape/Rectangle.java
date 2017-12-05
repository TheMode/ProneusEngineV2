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
        glBegin(filled ? GL_QUADS : GL_LINE_LOOP);

        glVertex2f(getX(), getY());
        glVertex2f(getX(), getHeight() + getY());
        glVertex2f(getWidth() + getX(), getHeight() + getY());
        glVertex2f(getWidth() + getX(), getY());

        glEnd();
    }

    @Override
    public boolean interact(float x, float y) {
        float scaledX = this.getX() / (float) Game.getDefaultWidth();
        float scaledWidth = this.getWidth() / (float) Game.getDefaultWidth();
        float scaledY = this.getY() / (float) Game.getDefaultHeight();
        float scaledHeight = this.getHeight() / (float) Game.getDefaultHeight();
        boolean result = scaledX <= x && scaledX + scaledWidth >= x && scaledY <= y && scaledY + scaledHeight >= y;
        return result;
    }

}

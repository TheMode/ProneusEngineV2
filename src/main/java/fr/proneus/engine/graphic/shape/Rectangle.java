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
        float x = getX() * (float) Game.getDefaultWidth();
        float y = getY() * (float) Game.getDefaultHeight();
        float width = getWidth() * (float) Game.getDefaultWidth();
        float height = getHeight() * (float) Game.getDefaultHeight();

        glBegin(filled ? GL_QUADS : GL_LINE_LOOP);

        glVertex2f(x, y);
        glVertex2f(x, height + y);
        glVertex2f(width + x, height + y);
        glVertex2f(width + x, y);

        glEnd();
    }

    @Override
    public boolean interact(float x, float y) {
        float scaledX = this.getX();
        float scaledWidth = this.getWidth();
        float scaledY = this.getY();
        float scaledHeight = this.getHeight();
        boolean result = scaledX <= x && scaledX + scaledWidth >= x && scaledY <= y && scaledY + scaledHeight >= y;
        return result;
    }

}

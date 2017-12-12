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
        // TODO verify for image
        float scaledWidth = getWidth() * (float) getTotalScaleX();
        float scaledHeight = getHeight() * (float) getTotalScaleY();
        float scaledX = getX() + (1 - scaledWidth) / 2;
        float scaledY = getY() + (1 - scaledHeight) / 2;
        System.out.println(getTotalScaleX() + " " + getTotalScaleY());
        // System.out.println(scaledX + " " + scaledY + " " + scaledWidth + " " + scaledHeight);
        boolean result = scaledX <= x && scaledX + scaledWidth >= x && scaledY <= y && scaledY + scaledHeight >= y;
        return result;
    }

}

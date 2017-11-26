package fr.proneus.engine.graphic.shape;

import fr.proneus.engine.graphic.Color;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Shape {

	public Rectangle(float x, float y, float width, float height, Color color, boolean filled) {
		super(x, y, width, height, color, filled);
	}
	
	public Rectangle(float x, float y, float width, float height) {
		this(x, y, width, height, Color.WHITE, true);
	}

	@Override
	public void render() {
		glBegin(isFilled() ? GL_QUADS : GL_LINE_LOOP);
		{

			glVertex2f(0, 0);
			glVertex2f(0, height);
			glVertex2f(width, height);
			glVertex2f(width, 0);

		}

		glEnd();
	}

	@Override
	public boolean interact(float x, float y) {
		return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
	}

}

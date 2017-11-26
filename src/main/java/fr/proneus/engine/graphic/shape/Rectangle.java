package fr.proneus.engine.graphic.shape;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import fr.proneus.engine.graphic.Color;

public class Rectangle extends Shape {

	public Rectangle(float x, float y, float width, float height, Color color) {
		super(x, y, width, height, color);
	}
	
	public Rectangle(float x, float y, float width, float height) {
		this(x, y, width, height, Color.WHITE);
	}

	@Override
	public void render() {
		glBegin(GL_QUADS);
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

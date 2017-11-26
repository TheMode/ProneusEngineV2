package fr.proneus.engine.graphic.shape;

import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import fr.proneus.engine.graphic.Color;

public class Circle extends Shape {

	private double radius;

	public Circle(float x, float y, Color color, double radius) {
		super(x, y, 0, 0, color);
		this.radius = radius;
	}

	public Circle(float x, float y, double radius) {
		this(x, y, Color.WHITE, radius);
	}

	@Override
	protected void render() {
		double precision = Math.PI / 128;

		glBegin(GL_POLYGON);

		for (double angle = 0; angle < 2 * Math.PI; angle += precision)
			glVertex2d(radius * Math.cos(angle), radius * Math.sin(angle));

		glEnd();

	}

	@Override
	public boolean interact(float x, float y) {
		double distance = Math.sqrt(Math.pow((this.x - x), 2) + Math.pow(this.y - y, 2));
		return Math.abs(distance) < radius;
	}

}

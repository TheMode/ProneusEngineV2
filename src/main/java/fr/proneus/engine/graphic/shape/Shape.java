package fr.proneus.engine.graphic.shape;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslatef;

import fr.proneus.engine.graphic.Color;

public abstract class Shape {

	private boolean filled;
	public float x, y, width, height;
	private int red, green, blue;
	private float alpha;
	private double angle;

	public Shape(float x, float y, float width, float height, Color color, boolean filled) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setColor(color);
		this.filled = filled;
	}

	public Shape(float x, float y, float width, float height) {
		this(x, y, width, height, Color.WHITE, true);
	}

	public void setColor(Color color) {
		this.red = color.r;
		this.green = color.g;
		this.blue = color.b;
		this.alpha = color.a;
	}

	public void rotate(double angle) {
		this.angle = angle;
	}

	public void draw() {
		glPushMatrix();
		glBindTexture(GL_TEXTURE_2D, 0);
		float x = this.x * 1920f;
		float y = this.y * 1080f;
		glTranslatef(x, y, 1);

		// Rotation
		if (angle != 0)
			glRotated(angle, 0, 0, 1);

		glColor4f((float) red / 255, (float) green / 255, (float) blue / 255, alpha);

		render();

		glColor4f(1, 1, 1, 1);

		glPopMatrix();
	}

    public boolean isFilled() {
        return filled;
    }

    protected abstract void render();

	public abstract boolean interact(float x, float y);

}

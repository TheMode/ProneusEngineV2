package fr.proneus.engine.graphic.particle;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.util.ArrayList;
import java.util.List;

import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Image;

public abstract class ParticleEffect {

	private ParticleType type;

	private Image image;
	private List<Particle> particles;

	public int step;
	public float defaultX, defaultY;
	public int maxStep;

	public ParticleEffect(ParticleType type, float defaultX, float defaultY, int maxStep) {
		this.type = type;
		this.defaultX = defaultX;
		this.defaultY = defaultY;
		this.maxStep = maxStep;

		this.particles = new ArrayList<>();
	}

	public ParticleEffect(Image image, float defaultX, float defaultY, int maxStep) {
		this(ParticleType.IMAGE, defaultX, defaultY, maxStep);
		this.image = image;
	}

	public abstract void updateParticles();

	public void draw() {
		if (step > maxStep) {
			return;
		}

		boolean isImage = type.equals(ParticleType.IMAGE);

		updateParticles();
		glPushMatrix();
		for (Particle particle : particles) {
			if (isImage) {
				glBindTexture(GL_TEXTURE_2D, image.getTextureID());
			}

			glColor4f((float) particle.color.r / 255, (float) particle.color.g / 255, (float) particle.color.g / 255,
					particle.color.a);
			if (isImage) {
				glTranslated(particle.x + defaultX, particle.y + defaultY, 1);
				float dcx = image.regionX / image.getImageWidth();
				float dcy = image.regionY / image.getImageHeight();

				float dcw = (image.regionX + image.regionWidth) / image.getImageWidth();
				float dch = (image.regionY + image.regionHeight) / image.getImageHeight();

				glBegin(GL_QUADS);
				glTexCoord2f(dcx, dcy);
				glVertex2d((image.regionX) + particle.x, (image.regionY) + particle.y);
				glTexCoord2f(dcw, dcy);
				glVertex2d((image.regionX + image.regionWidth) + particle.x, (image.regionY) + particle.y);
				glTexCoord2f(dcw, dch);
				glVertex2d((image.regionX + image.regionWidth) + particle.x,
						(image.regionY + image.regionHeight) + particle.y);
				glTexCoord2f(dcx, dch);
				glVertex2d((image.regionX) + particle.x, (image.regionY + image.regionHeight) + particle.y);

				glEnd();
			} else {
				glPointSize(particle.size);
				glBegin(GL_POINTS);
				glVertex2d(particle.x + defaultX, particle.y + defaultY);
				glEnd();
			}
		}
		glPopMatrix();
		particles.clear();
		step++;
	}

	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public enum ParticleType {
		POINT, IMAGE;
	}

	public class Particle {
		public double x, y;
		public int size = 1;
		public Color color = Color.WHITE;

	}

}

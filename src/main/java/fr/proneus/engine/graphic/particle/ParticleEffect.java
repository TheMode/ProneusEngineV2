package fr.proneus.engine.graphic.particle;

import java.util.ArrayList;
import java.util.List;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Image;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2f;

public abstract class ParticleEffect {

    public int step;
    public float defaultX, defaultY;
    public int maxStep;
    private ParticleType type;
    private Game game;
    private Image image;
    private List<Particle> particles;

    public ParticleEffect(ParticleType type, float defaultX, float defaultY, int maxStep) {
        this.type = type;
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.maxStep = maxStep;

        this.particles = new ArrayList<>();
    }

    public ParticleEffect(Game game, Image image, float defaultX, float defaultY, int maxStep) {
        this(ParticleType.IMAGE, defaultX, defaultY, maxStep);
        this.game = game;
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

                float regionX = image.getRegionX();
                float regionY = image.getRegionY();

                float regionWidth = image.getRegionWidth() / (float) Game.getDefaultWidth();
                float regionHeight = image.getRegionHeight() / (float) Game.getDefaultHeight();

                float imageWidth = image.getImagePixelWidth();
                float imageHeight = image.getImagePixelHeight();

                float x = particle.x * (float) game.getWidth();
                float y = particle.y * (float) game.getHeight();

                glTranslated(particle.x + defaultX, particle.y + defaultY, 1);
                float dcx = regionX / imageWidth;
                float dcy = regionY / imageHeight;

                float dcw = (regionX + regionWidth) / imageWidth;
                float dch = (regionY + regionHeight) / imageHeight;

                glBegin(GL_QUADS);

                glTexCoord2f(dcx, dcy);
                glVertex2f((regionX) + x, (regionY) + y);
                glTexCoord2f(dcw, dcy);
                glVertex2f((regionX + regionWidth) + x, (regionY) + y);
                glTexCoord2f(dcw, dch);
                glVertex2f((regionX + regionWidth) + x, (regionY + regionHeight) + y);
                glTexCoord2f(dcx, dch);
                glVertex2f((regionX) + x, (regionY + regionHeight) + y);

                glEnd();

                glPopMatrix();

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
        POINT, IMAGE
    }

    public class Particle {
        public float x, y;
        public int size = 1;
        public Color color = Color.WHITE;

    }

}

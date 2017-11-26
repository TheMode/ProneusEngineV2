package fr.proneus.engine.graphic.particle;

import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.utils.RandomUtils;

public class ParticleCircle extends ParticleEffect {

    public ParticleCircle(float x, float y) {
        super(ParticleType.POINT, x, y, 1000);

    }

    @Override
    public void updateParticles() {
        for (int i = 0; i < 360; i++) {
            float radius = (float) step / (float) maxStep * 250;
            float x = (float) Math.cos(Math.toRadians(i)) * radius;
            float y = (float) Math.sin(Math.toRadians(i)) * radius;

            int value = (int) (255 * ((float) i / 360f));

            Particle particle = new Particle();

            particle.color = new Color(value, step % 255, step % 255);

            particle.x = x + RandomUtils.getRandomFloat(-5, 5);
            particle.y = y + RandomUtils.getRandomFloat(-5, 5);
            particle.size = 1;
            addParticle(particle);
        }
    }

}

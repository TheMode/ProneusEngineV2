package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.particle.ParticleCircle;
import fr.proneus.engine.graphic.particle.ParticleEffect;
import fr.proneus.engine.input.MousePosition;
import fr.proneus.engine.state.State;

import java.util.ArrayList;
import java.util.List;

public class TestParticle extends State {

    private List<ParticleEffect> particles;

    @Override
    public void create(Game game) {
        this.particles = new ArrayList<>();
    }

    @Override
    public void update(Game game) {
        if (game.getInput().isMouseJustDown(0)) {
            MousePosition mouse = game.getInput().getMousePosition();
            particles.add(new ParticleCircle(mouse.getX(), mouse.getY()));
        }
    }

    @Override
    public void render(Game game, Graphics graphic) {

        for (ParticleEffect effects : particles) {
            effects.draw();
        }

    }

    @Override
    public void exit(Game game) {
    }

}

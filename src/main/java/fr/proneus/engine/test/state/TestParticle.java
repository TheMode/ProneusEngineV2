package fr.proneus.engine.test.state;

import java.util.ArrayList;
import java.util.List;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.particle.ParticleCircle;
import fr.proneus.engine.graphic.particle.ParticleEffect;
import fr.proneus.engine.input.MousePosition;
import fr.proneus.engine.state.State;

public class TestParticle extends State {

	private List<ParticleEffect> particles;

	@Override
	public void create(Game game) {
		this.particles = new ArrayList<>();
	}

	@Override
	public void update(Game game) {
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

	@Override
	public void onMouseDown(Game game, int key) {
        MousePosition mouse = game.getInput().getMousePosition();
        particles.add(new ParticleCircle(mouse.getX(), mouse.getY()));
	}

}

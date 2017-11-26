package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.proneus.engine.state.State;

public class TestAnimation extends State {

	private Sprite animation;

	@Override
	public void create(Game game) {

		animation = new Sprite(new Image("res/player.png"), 300, 300);

		AnimationFrame frame1 = new AnimationFrame(animation.getImage(), 0, 0, 7, 3);
		AnimationFrame frame2 = new AnimationFrame(animation.getImage(), 1, 0, 7, 3);
		AnimationFrame frame3 = new AnimationFrame(animation.getImage(), 2, 0, 7, 3);
		AnimationFrame frame4 = new AnimationFrame(animation.getImage(), 3, 0, 7, 3);
		AnimationFrame frame5 = new AnimationFrame(animation.getImage(), 4, 0, 7, 3);
		AnimationFrame frame6 = new AnimationFrame(animation.getImage(), 5, 0, 7, 3);
		AnimationFrame frame7 = new AnimationFrame(animation.getImage(), 6, 0, 7, 3);
		Animation anim = new Animation(frame1, frame2, frame3, frame4, frame5, frame6, frame7);

		animation.addAnimation("run", anim);
		animation.setAnimation("run", 50);

	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void render(Game game, Graphics graphic) {
		graphic.draw(animation);

	}

	@Override
	public void exit(Game game) {

	}

}

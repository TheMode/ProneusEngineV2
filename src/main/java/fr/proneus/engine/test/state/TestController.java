package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.Controller;
import fr.proneus.engine.input.Controller.ControllerAxe;
import fr.proneus.engine.input.Controller.ControllerButton;
import fr.proneus.engine.state.State;

public class TestController extends State {

	private float cameraSpeed;

	@Override
	public void create(Game game) {
		this.cameraSpeed = 10.5f;

	}

	@Override
	public void update(Game game) {
		Controller controller = game.getInput().getController(0);
		if (controller != null) {
			game.getCamera().move(controller.getJoyStickValue(ControllerAxe.JOYSTICK_1_HORIZONTAL, true) * cameraSpeed,
					-controller.getJoyStickValue(ControllerAxe.JOYSTICK_1_VERTICAL, true) * cameraSpeed);
		}

	}

	@Override
	public void render(Game game, Graphics graphic) {
		graphic.drawShape(new Rectangle(0, 0, game.getVirtualWidth(), game.getVirtualHeight(), Color.AQUA));

		Controller controller = game.getInput().getController(0);

		if (controller != null && controller.isButtonPressed(ControllerButton.A)) {
			graphic.drawString("A is pressed", 500, 500, Color.PURPLE);
		}
	}

	@Override
	public void exit(Game game) {

	}

}

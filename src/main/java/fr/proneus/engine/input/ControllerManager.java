package fr.proneus.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;

import org.lwjgl.glfw.GLFWJoystickCallback;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;

public class ControllerManager extends GLFWJoystickCallback {

	private Game game;

	private State state;

	public ControllerManager(Game game) {
		this.game = game;
	}

	@Override
	public void invoke(int joy, int event) {
		switch (event) {
		case GLFW_CONNECTED:
			state.onControllerConnect(game, joy);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onControllerConnect(joy);
			}
			break;
		case GLFW_DISCONNECTED:
			state.onControllerDisconnect(game, joy);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onControllerDisconnect(joy);
			}
			break;
		}

	}

	public Controller getController(int joy) {
		Controller controller = new Controller(joy);

		if (!controller.isConnected()) {
			return null;
		}

		return controller;
	}

	public void setListener(State state) {
		this.state = state;
	}

}

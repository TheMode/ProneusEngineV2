package fr.proneus.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;

public class MouseManager extends GLFWMouseButtonCallback {

	private Game game;

	private State state;

	protected boolean[] keys = new boolean[8];

	public MouseManager(Game game) {
		this.game = game;
	}

	@Override
	public void invoke(long window, int key, int scancode, int mods) {
		keys[key] = scancode != GLFW.GLFW_RELEASE;

		switch (scancode) {
		case 0:
			state.onMouseUp(game, key);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onMouseUp(game, key);
			}
			break;
		case 1:
			state.onMouseDown(game, key);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onMouseDown(game, key);
			}
			break;

		default:
			break;
		}

	}

	public void setListener(State state) {
		this.state = state;

	}

}

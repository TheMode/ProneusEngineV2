package fr.proneus.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import org.lwjgl.glfw.GLFWKeyCallback;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;

public class KeyboardManager extends GLFWKeyCallback {

	private Game game;
	
	public boolean[] keys = new boolean[65536];
	private State state;

	public KeyboardManager(Game game) {
		this.game = game;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
		
		
		switch (action) {
		case GLFW_RELEASE:
			state.onKeyUp(game, key, scancode);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onKeyUp(game, key, scancode);
			}
			break;
		case GLFW_PRESS:
			state.onKeyDown(game, key, scancode);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onKeyDown(game, key, scancode);
			}
			break;
		case GLFW_REPEAT:
			state.onKeyRepeat(game, key, scancode);
			for (Component comp : state.getComponents()) {
				if (comp.isVisible())
					comp.onKeyRepeat(game, key, scancode);
			}
			break;

		}

	}

	public void setListener(State state) {
		this.state = state;
	}

}

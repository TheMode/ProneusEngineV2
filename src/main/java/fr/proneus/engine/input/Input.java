package fr.proneus.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

import fr.proneus.engine.Game;

public class Input {

	private Game game;
	private KeyboardManager keyboard;
	private MousePositionManager mousePosition;
	private MouseManager mouse;
	private ControllerManager controller;

	public Input(Game game, KeyboardManager keyboard, MousePositionManager mousePosition, MouseManager mouse,
			ControllerManager controller) {
		this.game = game;
		this.keyboard = keyboard;
		this.mousePosition = mousePosition;
		this.mouse = mouse;
		this.controller = controller;
	}

	public boolean isKeyDown(int key) {
		return keyboard.keys[key];
	}

	public String getKeyReturn(int key, int scancode) {
		String keyString = glfwGetKeyName(key, scancode);
		if (keyString == null) {
			switch (key) {
			case GLFW_KEY_SPACE:
				return " ";

			default:
				return null;
			}
		}

		keyString = keyString.replace(" (PAVE NUM.)", "");

		return keyString;
	}

	public MousePosition getMousePosition() {
		return new MousePosition(game, mousePosition.x, mousePosition.y);
	}

	public MousePosition getVirtualMousePosition() {

		if (!game.isScalable())
			return getMousePosition();

		return new MousePosition(game,
				(int) ((float) game.getVirtualWidth() / (float) game.getWidth() * (float) mousePosition.x),
				(int) ((float) game.getVirtualHeight() / (float) game.getHeight() * (float) mousePosition.y));
	}

	public boolean isMouseDown(int key) {
		return mouse.keys[key];
	}

	public Controller getController(int joy) {
		return controller.getController(joy);
	}

}

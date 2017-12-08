package fr.proneus.engine.input;

import fr.proneus.engine.Game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

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

    public boolean isKeyUp(int key) {
        return !keyboard.keys[key];
    }

    public boolean isKeyJustDown(int key) {
        return keyboard.keysDown[key];
    }

    public boolean isKeyJustUp(int key) {
        return keyboard.keysUp[key];
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
        float x = mousePosition.x / (float) game.getWidth();
        float y = mousePosition.y / (float) game.getHeight();
        return new MousePosition(game, x, y);
    }

    public boolean isMouseDown(int key) {
        return mouse.keys[key];
    }

    public boolean isMouseUp(int key) {
        return !mouse.keys[key];
    }

    public boolean isMouseJustDown(int key) {
        return mouse.keysDown[key];
    }

    public boolean isMouseJustUp(int key) {
        return mouse.keysUp[key];
    }

    public Controller getController(int joy) {
        return controller.getController(joy);
    }

}

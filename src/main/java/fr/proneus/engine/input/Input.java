package fr.proneus.engine.input;

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

    public boolean isKeyUp(int key) {
        return !keyboard.keys[key];
    }

    public boolean isKeyJustDown(int key) {
        return keyboard.keysDown[key];
    }

    public boolean isKeyJustUp(int key) {
        return keyboard.keysUp[key];
    }

    public MousePosition getMousePosition() {
        float x = mousePosition.x / (float) game.getWidth();
        float y = mousePosition.y / (float) game.getHeight();
        return new MousePosition(game, x, y);
    }

    public boolean isMouseDown(int button) {
        return mouse.keys[button];
    }

    public boolean isMouseUp(int button) {
        return !mouse.keys[button];
    }

    public boolean isMouseJustDown(int button) {
        return mouse.keysDown[button];
    }

    public boolean isMouseJustUp(int button) {
        return mouse.keysUp[button];
    }

    public Controller getController(int joy) {
        return controller.getController(joy);
    }

}

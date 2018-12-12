package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.State;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseManager extends GLFWMouseButtonCallback {

    protected boolean[] keys = new boolean[8];
    protected boolean[] keysDown = new boolean[8];
    protected boolean[] keysUp = new boolean[8];
    private Game game;
    private State state;

    public MouseManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, int key, int scancode, int mods) {
        keys[key] = scancode != GLFW.GLFW_RELEASE;
        keysDown[key] = scancode == GLFW_PRESS;
        keysUp[key] = scancode == GLFW_RELEASE;

    }

    public void resetKeys() {
        this.keysDown = new boolean[8];
        this.keysUp = new boolean[8];
    }

    public void setListener(State state) {
        this.state = state;

    }

}

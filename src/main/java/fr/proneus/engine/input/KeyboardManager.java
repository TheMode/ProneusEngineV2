package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.State;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager extends GLFWKeyCallback {

    protected boolean[] keys = new boolean[65536];
    protected boolean[] keysDown = new boolean[65536];
    protected boolean[] keysUp = new boolean[65536];
    private Game game;
    private State state;

    public KeyboardManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == -1)
            return;
        keys[key] = action != GLFW_RELEASE;
        keysDown[key] = action == GLFW_PRESS;
        keysUp[key] = action == GLFW_RELEASE;

        if (action == GLFW_REPEAT)
            state.onKeyRepeat(key);
        if (action == GLFW_PRESS)
            state.onKeyDown(key);
        if (action == GLFW_RELEASE)
            state.onKeyUp(key);
    }

    public void resetKeys() {
        this.keysDown = new boolean[65536];
        this.keysUp = new boolean[65536];
    }

    public void setListener(State state) {
        this.state = state;
    }

}

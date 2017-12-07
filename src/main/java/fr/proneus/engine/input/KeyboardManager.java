package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager extends GLFWKeyCallback {

    public boolean[] keys = new boolean[65536];
    public boolean[] keysDown = new boolean[65536];
    public boolean[] keysUp = new boolean[65536];
    private Game game;
    private State state;

    public KeyboardManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
        keysDown[key] = action == GLFW_PRESS;
        keysUp[key] = action == GLFW_RELEASE;


        if (action == GLFW_REPEAT) {
            state.onKeyRepeat(game, key, scancode);
            for (Component comp : state.getComponents()) {
                if (comp.isVisible())
                    comp.onKeyRepeat(game, key, scancode);
            }
        }

    }

    public void resetKeys() {
        this.keysDown = new boolean[65536];
        this.keysUp = new boolean[65536];
    }

    public void setListener(State state) {
        this.state = state;
    }

}

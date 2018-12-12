package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.State;
import org.lwjgl.glfw.GLFWScrollCallback;

public class MouseScrollManager extends GLFWScrollCallback {

    private Game game;

    private State state;

    public MouseScrollManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, double x, double y) {
        state.onMouseScroll((float) y);
    }

    public void setListener(State state) {
        this.state = state;

    }

}

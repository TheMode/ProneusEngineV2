package fr.proneus.engine.input;

import org.lwjgl.glfw.GLFWScrollCallback;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;

public class MouseScrollManager extends GLFWScrollCallback {

    private Game game;

    private State state;

    public MouseScrollManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, double x, double y) {


        state.onMouseScroll(game, (float) y);
        for (Component comp : state.getComponents()) {
            if (comp.isVisible())
                comp.onMouseScroll(game, y);
        }

    }

    public void setListener(State state) {
        this.state = state;

    }

}

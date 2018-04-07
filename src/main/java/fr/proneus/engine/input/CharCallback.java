package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.state.State;
import org.lwjgl.glfw.GLFWCharModsCallback;

public class CharCallback extends GLFWCharModsCallback {

    private Game game;
    private State state;

    public CharCallback(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(long window, int code, int mods) {
        char character = (char) code;

        state.onCharCallback(game, character);
        for (Component comp : state.getComponents()) {
            if (comp.isVisible())
                comp.onCharCallback(game, character);
        }
    }

    public void setListener(State state) {
        this.state = state;
    }
}

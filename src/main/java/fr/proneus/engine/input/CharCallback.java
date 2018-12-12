package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.State;
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

        state.onCharCallback(character);
    }

    public void setListener(State state) {
        this.state = state;
    }
}

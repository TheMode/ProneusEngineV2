package fr.proneus.engine.input;

import fr.proneus.engine.Game;
import fr.proneus.engine.State;
import org.lwjgl.glfw.GLFWJoystickCallback;

import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;

public class ControllerManager extends GLFWJoystickCallback {

    private Game game;

    private State state;

    public ControllerManager(Game game) {
        this.game = game;
    }

    @Override
    public void invoke(int joy, int event) {
        switch (event) {
            case GLFW_CONNECTED:
                state.onControllerConnect(joy);
                break;
            case GLFW_DISCONNECTED:
                state.onControllerDisconnect(joy);
                break;
        }

    }

    public Controller getController(int joy) {
        Controller controller = new Controller(joy);
        return controller;
    }

    public void setListener(State state) {
        this.state = state;
    }

}

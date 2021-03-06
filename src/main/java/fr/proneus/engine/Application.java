package fr.proneus.engine;

import java.util.function.Consumer;

public class Application {

    private Game game;

    public Application(String display, int windowsWidth, int windowsHeight, int cameraWidth, int cameraHeight, State state) {
        this.game = new Game(display, windowsWidth, windowsHeight, cameraWidth, cameraHeight, state);
    }

    public void start() {
        game.start();
    }

    public void setCloseCallBack(Consumer<Game> callback) {
        game.setCloseCallBack(callback);
    }

    public void setIcon(String icon) {
        game.setIcon(icon);
    }

    public void setWidth(int width) {
        game.width = width;
    }

    public void setHeight(int height) {
        game.height = height;
    }

    public void setResizable(boolean resizable) {
        game.setResizable(resizable);
    }

    public void setVSync(boolean vsync) {
        game.setVSync(vsync);
    }

    // Only if VSync = false;
    public void setFpsLimit(int fps) {
        game.setMaxFPS(fps);
    }

    public void setTPS(int tps) {
        game.setTPS(tps);
    }

    public void enableDiscordRPC(String applicationID) {
        game.enableDiscordRPC(applicationID);
    }

}

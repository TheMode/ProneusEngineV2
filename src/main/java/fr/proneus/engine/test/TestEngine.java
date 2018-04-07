package fr.proneus.engine.test;

import fr.proneus.engine.Application;
import fr.proneus.engine.test.state.TestGUI;

public class TestEngine {

    public static void main(String[] args) {
        Application app = new Application("Breakout",
                1280, 720,
                1920, 1080,
                new TestGUI());

        app.setCloseCallBack(game -> {
            System.out.println("Game closed!");
        });
        // app.setFpsLimit(1);
        app.setTPS(60);
        //app.setBorderless();
        app.setIcon("/test2.png");
        //app.enableDiscordRPC("appId");
        app.start();
    }

}

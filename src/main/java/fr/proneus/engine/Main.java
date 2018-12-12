package fr.proneus.engine;

import fr.proneus.engine.demo.DemoState;

public class Main {

    public static void main(String[] args) {

        runGame();
    }

    private static void runGame() {
        int cameraWidth = 512;
        int cameraHeight = 288;

        String name = "Motable Client";
        State state = new DemoState();

        Application app = new Application(name,
                1920, 1080,
                cameraWidth, cameraHeight,
                state);

        app.setCloseCallBack(game -> {
            System.out.println("Game closed!");
        });

        // app.setFpsLimit(1);
        app.setTPS(60);
        app.setIcon("test2.png");
        //app.enableDiscordRPC("appId");
        app.start();
    }

}

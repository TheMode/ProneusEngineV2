package fr.proneus.engine;

import fr.proneus.engine.demo.fighter.MenuState;

public class Main {

    public static void main(String[] args) {

        runState(new MenuState());
    }

    private static void runState(State state) {
        int cameraWidth = 1920;
        int cameraHeight = 1080;

        String name = "Motable Client";

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

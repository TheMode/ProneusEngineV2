package fr.proneus.engine.test;

import fr.proneus.engine.Application;
import fr.proneus.engine.CloseCallBack;
import fr.proneus.engine.Game;
import fr.proneus.engine.test.state.TestDiscordIntegration;
import fr.proneus.engine.test.state.TestFileChooser;
import fr.proneus.engine.test.state.TestShooter;

public class TestEngine {

	public static void main(String[] args) {
        Application app = new Application("NexusFight", 1280, 720, new TestDiscordIntegration());

		app.setCloseCallBack(game -> {
            System.out.println("Game closed!");
        });
        //app.setFpsLimit(60);
		app.setTPS(60);
		//app.setBorderless();
		app.setScale(true);
		app.setIcon("/test2.png");
		app.enableDiscordRPC("appId");
		app.start();
	}

}
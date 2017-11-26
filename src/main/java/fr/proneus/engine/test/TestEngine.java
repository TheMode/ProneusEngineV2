package fr.proneus.engine.test;

import fr.proneus.engine.Application;
import fr.proneus.engine.CloseCallBack;
import fr.proneus.engine.Game;
import fr.proneus.engine.test.state.TestShooter;

public class TestEngine {

	public static void main(String[] args) {
		Application app = new Application("NexusFight", 1280, 720, new TestShooter());

		app.setCloseCallBack(new CloseCallBack() {

			@Override
			public void onClose(Game game) {

			}
		});
		app.setFpsLimit(60);
		app.setTPS(60);
		//app.setBorderless();
		app.setScale(true);
		app.setIcon("res/test2.png");
		// app.enableDiscordRPC("381903221783789568");
		app.start();
	}

}
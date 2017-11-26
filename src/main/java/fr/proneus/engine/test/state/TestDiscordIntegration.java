package fr.proneus.engine.test.state;

import java.util.concurrent.TimeUnit;

import fr.proneus.engine.Game;
import fr.proneus.engine.discord.DiscordRPCInfo;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.state.State;

public class TestDiscordIntegration extends State {

	@Override
	public void create(Game game) {

		if (game.hasDiscordRPCEnabled()) {
			DiscordRPCInfo info = new DiscordRPCInfo();
			info.setState("Current State");
			info.setDetails("Details about the game");
			// info.setStartTimestamp(System.currentTimeMillis());
			info.setEndTimestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(20));
			info.setParty("ID", 2, 10);
			game.updateDiscordRPC(info);
		}
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void render(Game game, Graphics graphic) {

	}

	@Override
	public void exit(Game game) {

	}

}

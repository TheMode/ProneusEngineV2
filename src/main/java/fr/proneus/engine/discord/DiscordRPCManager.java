package fr.proneus.engine.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordJoinRequest;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.proneus.engine.Game;
import fr.proneus.engine.state.State;

public class DiscordRPCManager {

	private DiscordRPC discordRPC;

	public DiscordRPCManager() {

	}

	public void connect(Game game, String applicationID) {
		State state = game.getState();
		discordRPC = DiscordRPC.INSTANCE;
		DiscordEventHandlers handler = new DiscordEventHandlers();
		handler.ready = new DiscordEventHandlers.OnReady() {

			@Override
			public void accept() {
				state.onDiscordRPCReady(game);

			}
		};
		handler.disconnected = new DiscordEventHandlers.OnStatus() {

			@Override
			public void accept(int arg0, String arg1) {
				state.onDiscordRPCDisconnected(game);

			}
		};
		handler.disconnected = new DiscordEventHandlers.OnStatus() {

			@Override
			public void accept(int arg0, String arg1) {
				state.onDiscordRPCErrored(game);

			}
		};
		handler.joinGame = new DiscordEventHandlers.OnGameUpdate() {

			@Override
			public void accept(String arg0) {
				state.onDiscordRPCJoinGame(game);

			}
		};
		handler.spectateGame = new DiscordEventHandlers.OnGameUpdate() {

			@Override
			public void accept(String arg0) {
				state.onDiscordRPCSpectateGame(game);

			}
		};
		handler.joinRequest = new DiscordEventHandlers.OnJoinRequest() {

			@Override
			public void accept(DiscordJoinRequest arg0) {
				state.onDiscordRPCJoinRequest(game);

			}
		};
		discordRPC.Discord_Initialize(applicationID, handler, true, "");
	}

	public void update(DiscordRPCInfo info) {
		DiscordRichPresence pre = new DiscordRichPresence();
		pre.state = info.getState();
		pre.details = info.getDetails();
		if (info.getStartTimestamp() != 0)
			pre.startTimestamp = info.getStartTimestamp();
		if (info.getEndTimestamp() != 0)
			pre.endTimestamp = info.getEndTimestamp();
		pre.largeImageKey = info.getLargeImageKey();
		pre.largeImageText = info.getLargeImageText();
		pre.smallImageKey = info.getSmallImageKey();
		pre.smallImageText = info.getSmallImageText();
		pre.partyId = info.getPartyId();
		pre.partySize = info.getPartySize();
		pre.partyMax = info.getPartyMax();

		if (info.getMatchSecret() != null)
			pre.matchSecret = info.getMatchSecret();
		if (info.getJoinSecret() != null)
			pre.joinSecret = info.getJoinSecret();
		if (info.getSpectateSecret() != null)
			pre.spectateSecret = info.getSpectateSecret();

		pre.instance = info.getInstance();
		discordRPC.Discord_UpdatePresence(pre);
	}

	public DiscordRPC getDiscordRPC() {
		return discordRPC;
	}

}

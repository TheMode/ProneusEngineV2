package fr.proneus.engine.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.proneus.engine.Game;
import fr.proneus.engine.State;

public class DiscordRPCManager {

    private DiscordRPC discordRPC;
    private String applicationId;
    private boolean isOnSteam;

    public DiscordRPCManager(boolean isOnSteam, String applicationId) {
        this.discordRPC = DiscordRPC.INSTANCE;
        this.applicationId = applicationId;
        this.isOnSteam = isOnSteam;
    }

    public void connect(Game game) {
        State state = game.getState();
        DiscordEventHandlers handler = new DiscordEventHandlers();
        handler.ready = (user) -> state.onDiscordRPCReady();
        handler.disconnected = (error, message) -> state.onDiscordRPCDisconnected(message);
        handler.errored = (error, message) -> state.onDiscordRPCError(message);
        handler.joinGame = secret -> state.onDiscordRPCJoinGame(secret);
        handler.spectateGame = secret -> state.onDiscordRPCSpectateGame(secret);
        handler.joinRequest = request -> {
            DiscordRPCJoinRequest joinRequest = new DiscordRPCJoinRequest(request.userId, request.username, request.discriminator, request.avatar);
            state.onDiscordRPCJoinRequest(joinRequest);
        };
        this.discordRPC.Discord_Initialize(this.applicationId, handler, true, this.isOnSteam ? this.applicationId : "");
    }

    public void disconnect() {
        this.discordRPC.Discord_Shutdown();
    }

    public void respond(String userId, Response response) {
        this.discordRPC.Discord_Respond(userId, response.getId());
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

    public String getApplicationId() {
        return applicationId;
    }

    public enum Response {
        NO(0),
        YES(1),
        IGNORE(2);

        private int id;

        Response(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

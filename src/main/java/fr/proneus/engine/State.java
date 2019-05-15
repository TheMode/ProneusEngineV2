package fr.proneus.engine;

import fr.proneus.engine.discord.DiscordRPCJoinRequest;
import fr.proneus.engine.graphic.Graphics;

public abstract class State {

    private Game game;

    public abstract void create();

    public abstract void update();

    public abstract void render(Graphics graphics);

    public abstract void exit();

    public void onKeyRepeat(int key) {
    }

    public void onKeyDown(int key) {
    }

    public void onKeyUp(int key) {
    }

    public void onCharCallback(char character) {
    }

    public void onMouseMove(float x, float y) {
    }

    public void onMouseScroll(float power) {
    }

    public void onControllerConnect(int joy) {
    }

    public void onControllerDisconnect(int joy) {
    }

    // Discord
    public void onDiscordRPCReady() {
    }

    public void onDiscordRPCDisconnected(String message) {
    }

    public void onDiscordRPCError(String message) {
    }

    public void onDiscordRPCJoinGame(String secret) {
    }

    public void onDiscordRPCSpectateGame(String secret) {
    }

    public void onDiscordRPCJoinRequest(DiscordRPCJoinRequest joinRequest) {
    }

    public Game getGame() {
        return game;
    }

    protected void setGame(Game game) {
        this.game = game;
    }

}

package fr.proneus.engine.test.state.breakout;

import fr.proneus.engine.Game;
import fr.proneus.engine.input.Buttons;
import fr.proneus.engine.input.Controller;

public class PlayerCharge {

    private Game game;

    // In milliseconds
    private int increaseLoadingTime = 3000;
    private int decreaseLoadingTime = 3000;

    private boolean isLoading;
    private long increaseLoadingStart;
    private long decreaseLoadingStart;

    private float increaseCharge;
    private float decreaseCharge;

    private Controller controller;

    public PlayerCharge(Game game) {
        this.controller = game.getInput().getController(0);
        this.game = game;
    }

    public void update() {
        boolean increaseCheck = this.controller.getJoyStickValue(Controller.ControllerAxe.RT, true) > 0.5f || game.getInput().isMouseDown(Buttons.RIGHT);
        boolean decreaseCheck = this.controller.getJoyStickValue(Controller.ControllerAxe.LT, true) > 0.5f || game.getInput().isMouseDown(Buttons.LEFT);
        isLoading = increaseCheck || decreaseCheck;
        if (decreaseCheck) {
            // Reset increase
            increaseCharge = 0;
            increaseLoadingStart = 0;

            if (decreaseLoadingStart == 0)
                decreaseLoadingStart = System.currentTimeMillis();

            decreaseCharge = (float) Math.min(System.currentTimeMillis() - (decreaseLoadingStart + decreaseLoadingTime) + decreaseLoadingTime, decreaseLoadingTime) / (float) decreaseLoadingTime;
        } else if (increaseCheck) {
            // Reset increase
            decreaseCharge = 0;
            decreaseLoadingStart = 0;

            if (increaseLoadingStart == 0)
                increaseLoadingStart = System.currentTimeMillis();

            increaseCharge = (float) Math.min(System.currentTimeMillis() - (increaseLoadingStart + increaseLoadingTime) + increaseLoadingTime, increaseLoadingTime) / (float) increaseLoadingTime;
        } else {
            resetLoading();
        }

        System.out.println(decreaseCharge + " : " + increaseCharge);

    }


    public boolean isLoading() {
        return isLoading;
    }

    public float getIncreaseCharge() {
        return increaseCharge;
    }

    public float getDecreaseCharge() {
        return decreaseCharge;
    }

    public void resetCharge() {
        increaseCharge = 0;
        decreaseCharge = 0;
        resetLoading();
    }

    private void resetLoading() {
        increaseLoadingStart = 0;
        decreaseLoadingStart = 0;
        isLoading = false;
    }
}

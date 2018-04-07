package fr.proneus.engine.test.state.breakout;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.input.Controller;

public class Ball extends Sprite {

    private static final float SPEED = 0.5f / 60f;
    private Player player;
    // Speed manager
    private float currentSpeed;
    private float totalCharge;
    private float increasePerHit = 1.02f;
    private int hitNumber;

    private float destX = 0, destY = 1;

    public Ball(Player player, float x, float y) {
        super(new Image("/breakout/ball.png"), x, y);
        this.player = player;

    }

    @Override
    public void update(Game game) {
        super.update(game);

        Controller controller = game.getInput().getController(0);

        // Player check
        if (this.overlaps(player.getLauncherHitBox()) && destY > 0 && !player.getCharge().isLoading()) {
            refreshCurrentSpeed();

            float horizontal = controller.isConnected() ?
                    controller.getJoyStickValue(Controller.ControllerAxe.JOYSTICK_2_HORIZONTAL, true) :
                    (game.getInput().getMousePosition().getX() * 2 - 1);
            destX = horizontal;
            destX *= currentSpeed;

            destY = -1;
            destY *= currentSpeed;

            player.getCharge().resetCharge();
        }

        // Top Check
        if (getY() <= 0) {
            destY = -destY;
        }

        // Left/Right check
        if (getX() + getWidth() >= 1) {
            destX = -destX;
        } else if (getX() <= 0) {
            destX = -destX;
        }

        // Move
        setX(getX() + destX * SPEED);
        setY(getY() + destY * SPEED);

    }

    private void refreshCurrentSpeed() {
        totalCharge += player.getCharge().getIncreaseCharge() - player.getCharge().getDecreaseCharge();
        totalCharge = Math.max(0, totalCharge);
        hitNumber++;

        // Each complete charge means x1.5
        currentSpeed = 1 + totalCharge * 1.5f;

        float totalHitIncrease = increasePerHit;
        for (int i = 0; i < hitNumber - 1; i++) {
            totalHitIncrease += increasePerHit - 1;
        }

        currentSpeed *= totalHitIncrease;
    }
}

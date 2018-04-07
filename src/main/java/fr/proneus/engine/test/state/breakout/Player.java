package fr.proneus.engine.test.state.breakout;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.Controller;
import fr.proneus.engine.input.Keys;

public class Player extends Sprite {

    private Rectangle launcher;
    private Controller controller;

    private float speed = 6f;

    private Rectangle wall;

    private PlayerCharge charge;

    public Player(Game game, Rectangle wall, float x, float y) {
        super(new Image("/breakout/player.png"), x, y);

        setupAnimation();
        this.launcher = new Rectangle(getX(), getY(), 0.1f, 0.05f);
        this.controller = game.getInput().getController(0);

        this.wall = wall;

        this.charge = new PlayerCharge(game);
    }

    @Override
    public void update(Game game) {
        super.update(game);

        // Movement
        float v = 0;
        v += game.getInput().isKeyDown(Keys.W) ? -1 : 0;
        v += game.getInput().isKeyDown(Keys.S) ? 1 : 0;
        float h = 0;
        h += game.getInput().isKeyDown(Keys.A) ? -1 : 0;
        h += game.getInput().isKeyDown(Keys.D) ? 1 : 0;

        if (this.controller.isConnected()) {
            v += this.controller.getJoyStickValue(Controller.ControllerAxe.JOYSTICK_1_VERTICAL, true);
            h += this.controller.getJoyStickValue(Controller.ControllerAxe.JOYSTICK_1_HORIZONTAL, true);
        }

        float newX = launcher.getX() + h / 1000 * speed;
        float newY = launcher.getY() + v / 1000 * speed;

        newX = newX < 0 ? 0 : newX > 1 ? 1 : newX;
        newY = newY < 0 ? 0 : newY > 1 ? 1 : newY;

        launcher.setX(wall.interact(newX, launcher.getY()) || !game.getCamera().isFullyVisible(newX, launcher.getY(), launcher.getWidth(), launcher.getHeight()) ?
                launcher.getX() : newX);
        launcher.setY(wall.interact(launcher.getX(), newY) || !game.getCamera().isFullyVisible(launcher.getX(), newY, launcher.getWidth(), launcher.getHeight()) ?
                launcher.getY() : newY);

        setX(launcher.getX());
        setY(launcher.getY());

        // Charge
        this.charge.update();
    }

    public void setupAnimation() {
        AnimationFrame idleFrame1 = new AnimationFrame(getImage(), 0, 0, 5, 2);
        AnimationFrame idleFrame2 = new AnimationFrame(getImage(), 1, 0, 5, 2);
        AnimationFrame idleFrame3 = new AnimationFrame(getImage(), 2, 0, 5, 2);
        AnimationFrame idleFrame4 = new AnimationFrame(getImage(), 3, 0, 5, 2);
        AnimationFrame idleFrame5 = new AnimationFrame(getImage(), 4, 0, 5, 2);
        Animation idleAnim = new Animation(idleFrame1, idleFrame2, idleFrame3, idleFrame4, idleFrame5);

        AnimationFrame runFrame1 = new AnimationFrame(getImage(), 0, 1, 5, 2);
        AnimationFrame runFrame2 = new AnimationFrame(getImage(), 1, 1, 5, 2);
        AnimationFrame runFrame3 = new AnimationFrame(getImage(), 2, 1, 5, 2);
        AnimationFrame runFrame4 = new AnimationFrame(getImage(), 3, 1, 5, 2);
        Animation runAnim = new Animation(runFrame1, runFrame2, runFrame3, runFrame4);

        addAnimation("idle", idleAnim);
        addAnimation("run", runAnim);

        setAnimation("idle", 100);
    }

    public PlayerCharge getCharge() {
        return charge;
    }

    public Rectangle getLauncherHitBox() {
        return launcher;
    }
}

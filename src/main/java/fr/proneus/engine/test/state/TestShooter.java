package fr.proneus.engine.test.state;

import java.util.ArrayList;
import java.util.List;

import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.graphic.shape.RoundedRectangle;
import fr.proneus.engine.script.Script;
import fr.proneus.engine.script.ScriptArgs;
import fr.proneus.engine.script.ScriptValue;
import fr.proneus.engine.utils.FileUtils;
import fr.proneus.engine.utils.Vector;
import org.luaj.vm2.Varargs;
import org.lwjgl.glfw.GLFW;

import fr.proneus.engine.Game;
import fr.proneus.engine.Game.WindowType;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.input.Buttons;
import fr.proneus.engine.input.Controller;
import fr.proneus.engine.input.MousePosition;
import fr.proneus.engine.input.Controller.ControllerAxe;
import fr.proneus.engine.input.Controller.JoyStick;
import fr.proneus.engine.light.Light;
import fr.proneus.engine.light.Light.LightType;
import fr.proneus.engine.state.State;
import fr.proneus.engine.utils.MathUtils;

public class TestShooter extends State {

    private Sprite player;
    private float speed = 3.5f;

    private long laserCooldown = 50;
    private long lastShoot;

    private Image laserImage;

    private List<Sprite> laserList = new ArrayList<>();
    private List<Sprite> lasertoRemove = new ArrayList<>();

    private Controller controller;

    private Light light;

    @Override
    public void create(Game game) {
        this.player = new Sprite(new Image("/ship.png"), 0.5f, 0.5f);
        addSprite(player);
        this.laserImage = new Image("/shoot.png");

        this.controller = game.getInput().getController(0);

        light = new Light(0.5f, 0.5f);
        light.setColor(Color.WHITE);
        light.setLightType(LightType.AMBIENT);
        light.setAmbientColor(0.5f);
        light.setAmbientIntensity(1.5f);
        light.setAmbientDecrease(1f);
        // light.setAmbientShowRange(1f);

        getLightManager().addLight(light);
        Script script = game.getScriptManager().loadScript("/scripts/testscript.lua");
        ScriptValue scriptValue = script.runMethod("testFunct", player);
        ScriptArgs scriptArgs = scriptValue.getArg(1);
        System.out.println("test: " + scriptArgs.toString());
    }

    @Override
    public void update(Game game) {
        // Angle
        if (!this.controller.isConnected()) {
            MousePosition mouse = game.getInput().getMousePosition();
            double mouseAngle = MathUtils.getAngle(player.getX(), player.getY(), mouse.getX(), mouse.getY());
            player.angle = Math.toDegrees(mouseAngle) + 90;
        } else {
            double angle = this.controller.getJoystickAngle(JoyStick.JOYSTICK_2, true);
            if (angle != 0) {
                player.angle = angle;
            }
        }

        // Movement
        float v = 0;
        v += game.getInput().isKeyDown(GLFW.GLFW_KEY_W) ? -1 : 0;
        v += game.getInput().isKeyDown(GLFW.GLFW_KEY_S) ? 1 : 0;
        float h = 0;
        h += game.getInput().isKeyDown(GLFW.GLFW_KEY_A) ? -1 : 0;
        h += game.getInput().isKeyDown(GLFW.GLFW_KEY_D) ? 1 : 0;

        if (this.controller.isConnected()) {
            v += this.controller.getJoyStickValue(ControllerAxe.JOYSTICK_1_VERTICAL, true);
            h += this.controller.getJoyStickValue(ControllerAxe.JOYSTICK_1_HORIZONTAL, true);
        }

        // Dash
        if (this.controller.getJoyStickValue(ControllerAxe.LT, false) > 0 && player.getForces().size() == 0) {
            MousePosition mouse = game.getInput().getMousePosition();
            double angle = player.angle - 90;
            MathUtils.AngleValue value = MathUtils.getMoveValue(angle, player.getX(), player.getY(), 1f, 1f);
            player.applyForce(new Vector(value.x / 100, value.y / 100));
        }

        player.move(h / 1000 * speed, v / 1000 * speed);

        // Shoot
        if (game.getInput().isMouseDown(Buttons.LEFT)
                || this.controller.isConnected() && this.controller.getJoyStickValue(ControllerAxe.RT, false) > 0) {
            if (System.currentTimeMillis() - lastShoot >= laserCooldown) {
                Sprite laser = new Sprite(laserImage, player.getX(), player.getY());
                laser.angle = player.angle - 90;
                laser.moveFromAngle(0.002f, 0.002f);
                addSprite(laser);
                laserList.add(laser);

                lastShoot = System.currentTimeMillis();
            }
        }

        for (Sprite laser : laserList) {
            if (!game.getCamera().isPartiallyVisible(laser)) {
                System.out.println("test");
                removeSprite(laser);
                lasertoRemove.add(laser);
                continue;
            }

            laser.moveFromAngle(0.002f, 0.002f);
        }

        laserList.removeAll(lasertoRemove);

        lasertoRemove.clear();

    }

    @Override
    public void render(Game game, Graphics graphic) {
        player.draw(graphic);
        for (Sprite laser : laserList) {
            laser.draw(graphic);
        }

        graphic.drawString("SALUT", 0.5f, 0.5f);

    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public void onKeyUp(Game game, int key, int scancode) {
        if (key == GLFW.GLFW_KEY_T) {
            game.changeWindow(WindowType.BORDERLESS);
        } else if (key == GLFW.GLFW_KEY_Y) {
            game.changeWindow(WindowType.NORMAL);
        }
    }

    @Override
    public void onMouseScroll(Game game, float power) {
        game.getCamera().zoom(power * 0.5f, power * 0.5f);
    }

    @Override
    public void onMouseMove(Game game, float x, float y) {
        light.setPosition(x, y);
    }

}

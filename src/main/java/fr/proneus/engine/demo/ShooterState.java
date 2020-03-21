package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.Texture;
import fr.proneus.engine.input.Input;
import fr.proneus.engine.input.Keys;
import fr.proneus.engine.input.MousePosition;
import fr.themode.utils.MathUtils;
import fr.themode.utils.file.FileUtils;

public class ShooterState extends State {

    private Texture shipTexture;
    private Sprite ship;

    @Override
    public void create() {
        this.shipTexture = new Texture(FileUtils.getInternalFile("ship.png"));
        this.ship = new Sprite(shipTexture);
        this.ship.setPosition(0.5f, 0.5f);
        this.ship.scale(3, 3);
    }

    @Override
    public void update() {
        // Rotation
        MousePosition mousePosition = getGame().getInput().getMousePosition();
        this.ship.setAngle((float) MathUtils.getAngle(ship.getX(), ship.getY(), mousePosition.getX(), mousePosition.getY()) - 90f);
        ship.setColor(Color.BLUE);

        // Movement
        float speed = 0.0075f;
        Input input = getGame().getInput();
        float h = input.isKeyDown(Keys.A) ? -1 : input.isKeyDown(Keys.D) ? 1 : 0f;
        float v = input.isKeyDown(Keys.W) ? -1 : input.isKeyDown(Keys.S) ? 1 : 0f;

        this.ship.move(h * speed, v * speed);

        boolean visible = getGame().getCamera().isFullyVisible(ship);
        //System.out.println("visible: " + visible);
    }

    @Override
    public void render(Graphics graphics) {
        ship.draw(graphics);
    }

    @Override
    public void exit() {

    }
}
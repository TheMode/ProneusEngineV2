package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.graphic.*;
import fr.themode.utils.RandomUtils;
import fr.themode.utils.file.FileUtils;

public class SampleState extends State {

    private Texture shipTexture;
    private Texture bitmapTexture;

    private Sprite ship;
    private Sprite bitmapSprite;

    @Override
    public void create() {
        this.shipTexture = new Texture(FileUtils.getInternalFile("ship.png"));

        Sprite sprite = new Sprite(shipTexture);
        sprite.setPosition(RandomUtils.getRandomFloat(0, 1), RandomUtils.getRandomFloat(0, 1));
        this.ship = sprite;


        Bitmap bitmap = new Bitmap(2, 2);
        bitmap.setPixel(0, 0, Color.YELLOW);
        bitmap.setPixel(0, 1, Color.BLUE);
        bitmap.setPixel(1, 0, Color.PURPLE);
        bitmap.setPixel(1, 1, Color.WHITE);
        this.bitmapTexture = new Texture(bitmap);

        this.bitmapSprite = new Sprite(bitmapTexture);
        bitmapSprite.setPosition(RandomUtils.getRandomFloat(0, 1), RandomUtils.getRandomFloat(0, 1));
        bitmapSprite.scale(50, 50);
    }

    @Override
    public void update() {
        //System.out.println(getGame().getFps());
    }

    @Override
    public void render(Graphics graphics) {
        ship.draw(graphics);
        bitmapSprite.draw(graphics);
    }

    @Override
    public void exit() {

    }
}

package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.Texture;
import fr.themode.utils.RandomUtils;
import fr.themode.utils.file.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class SpritePerfTestState extends State {

    private Texture shipTexture;

    private List<Sprite> ships = new ArrayList<>();

    @Override
    public void create() {
        this.shipTexture = new Texture(FileUtils.getInternalFile("ship.png"));
        for (int i = 0; i < 1; i++) {
            Sprite sprite = new Sprite(shipTexture);
            sprite.setPosition(RandomUtils.getRandomFloat(0, 1), RandomUtils.getRandomFloat(0, 1));
            this.ships.add(sprite);
        }
    }

    @Override
    public void update() {
        System.out.println(getGame().getFps());
    }

    @Override
    public void render(Graphics graphics) {
        for (Sprite sprite : ships) {
            sprite.draw(graphics);
        }
    }

    @Override
    public void exit() {

    }
}

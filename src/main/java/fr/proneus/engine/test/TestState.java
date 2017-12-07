package fr.proneus.engine.test;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.state.State;

public class TestState extends State {

    public Sprite sprite;
    public double scale = 1;

    @Override
    public void create(Game game) {
        this.sprite = new Sprite(new Image("res/ship.png"), 100, 100);
    }

    @Override
    public void update(Game game) {
        System.out.println("Scale : " + scale);
    }

    @Override
    public void render(Game game, Graphics graphic) {
        this.sprite.setScale(scale);
        graphic.draw(sprite);
    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public void onMouseScroll(Game game, float power) {
        scale = scale + power * .05f;
    }

}

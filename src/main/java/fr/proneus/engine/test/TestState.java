package fr.proneus.engine.test;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Renderable;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;
import fr.proneus.engine.state.State;

public class TestState extends State {

    private Renderable rect;
    private Renderable player;

    @Override
    public void create(Game game) {
        this.rect = createRenderable(new Rectangle(0, 0, 1, 1));
        this.player = this.createRenderable(new Sprite(new Image("/ship.png"), 0.5f, 0.5f));
        this.player.setDrawType(Renderable.DrawType.CENTERED);
    }

    @Override
    public void update(Game game) {
        MousePosition mouse = game.getInput().getMousePosition();
        System.out.println(rect.interact(mouse.getX(), mouse.getY()));
    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(rect);
        graphic.draw(player);
    }

    @Override
    public void exit(Game game) {

    }

    @Override
    public void onMouseScroll(Game game, float power) {
        float scale = power * 0.1f;
        game.getCamera().zoom(scale, scale);
        System.out.println(game.getCamera().getZoomX() + " " + game.getCamera().getZoomY());
    }

}

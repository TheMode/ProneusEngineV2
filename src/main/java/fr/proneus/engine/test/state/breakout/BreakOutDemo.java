package fr.proneus.engine.test.state.breakout;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.state.State;

public class BreakOutDemo extends State {

    private Rectangle wall;

    private Player player;

    private Sprite ball;

    @Override
    public void create(Game game) {
        this.wall = this.createRenderable(new Rectangle(0, 0, 1f, 0.75f));
        this.wall.setColor(Color.RED);
        this.player = this.createRenderable(new Player(game, wall, 0.5f, 0.8f));
        this.ball = this.createRenderable(new Ball(player, 0.5f, 0.5f));
    }

    @Override
    public void update(Game game) {

    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(wall);
        graphic.draw(player.getLauncherHitBox());
        graphic.draw(player);
        graphic.draw(ball);
    }

    @Override
    public void exit(Game game) {

    }
}

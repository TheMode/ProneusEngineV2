package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Font.FontStyle;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;

public abstract class ImageButton extends Component {

    private Rectangle rect;
    private Sprite sprite, hoverSprite;

    private String text;
    private boolean hover;

    public ImageButton(String text, float x, float y, Image image, Image hoverImage) {
        this.sprite = new Sprite(image, x, y);
        this.sprite.applyCameraZoom(false);
        this.hoverSprite = new Sprite(hoverImage, x, y);
        this.hoverSprite.applyCameraZoom(false);
        this.rect = new Rectangle(x, y, image.getRegionWidth(), image.getRegionHeight());
        this.rect.applyCameraZoom(false);
        this.text = text;

    }

    @Override
    public void update(Game game) {
        MousePosition mouse = game.getInput().getMousePosition().toCameraPosition();

        this.hover = this.rect.interact(mouse.getX(), mouse.getY());

        if (game.getInput().isMouseJustDown(0)) {
            if (hover)
                onClick();
        }
    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(hover ? hoverSprite : sprite);
        graphic.drawString(text, rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2, FontStyle.CENTERED, Color.WHITE);
    }

    public abstract void onClick();

}

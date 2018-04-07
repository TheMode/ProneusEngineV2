package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;

public abstract class ImageButton extends Component {

    private Rectangle rect;
    private Sprite defaultSprite, hoverSprite, pressSprite;
    private boolean hover;
    private boolean pressed;

    public ImageButton(float x, float y, Image image, Image hoverImage, Image pressImage) {
        this.defaultSprite = new Sprite(image, x, y);
        this.defaultSprite.applyCameraZoom(false);
        this.hoverSprite = new Sprite(hoverImage, x, y);
        this.hoverSprite.applyCameraZoom(false);
        this.pressSprite = new Sprite(pressImage, x, y);
        this.pressSprite.applyCameraZoom(false);
        this.rect = new Rectangle(x, y,
                image.getRegionWidth() * image.getImageWidth(),
                image.getRegionHeight() * image.getImageHeight());
        this.rect.applyCameraZoom(false);

    }

    @Override
    public void update(Game game) {
        if (game.getInput().isMouseJustUp(0)) {
            if (pressed)
                onClick();
        }
        MousePosition mouse = game.getInput().getMousePosition().toCameraPosition();

        this.hover = this.rect.interact(mouse.getX(), mouse.getY());
        this.pressed = hover && game.getInput().isMouseDown(0);
        this.hover = pressed == false;
    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(hover ? hoverSprite : pressed ? pressSprite : defaultSprite);
    }

    public abstract void onClick();

}

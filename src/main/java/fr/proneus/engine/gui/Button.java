package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Font.FontStyle;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;

public abstract class Button extends Component {

    private Rectangle rect;
    private Rectangle border;

    private String text;
    private boolean hover;

    public Button(String text, float x, float y, float width, float height) {
        this.rect = new Rectangle(x, y, width, height, Color.GRAY, true);
        this.rect.applyCameraZoom(false);
        float borderSize = 0.005f;
        this.border = new Rectangle(x - borderSize, y - borderSize, width + borderSize * 2, height + borderSize * 2, getColor(), true);
        this.border.applyCameraZoom(false);
        this.text = text;
    }

    @Override
    public void update(Game game) {
        MousePosition mouse = game.getInput().getMousePosition().toCameraPosition();
        this.hover = this.rect.interact(mouse.getX(), mouse.getY());

        this.border.setColor(getColor());

        if (game.getInput().isMouseJustUp(0)) {
            if (hover)
                onClick();
        }
    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(border);
        graphic.draw(rect);
        float buttonX = rect.getX() + (rect.getWidth() / 2);
        float buttonY = rect.getY() + (rect.getHeight() / 2);
        graphic.drawString(text, buttonX, buttonY, FontStyle.CENTERED, getColor());
    }

    private Color getColor() {
        return this.hover ? Color.WHITE : Color.BLACK;
    }

    public abstract void onClick();

}

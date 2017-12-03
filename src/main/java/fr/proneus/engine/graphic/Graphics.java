package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Font.FontStyle;
import fr.proneus.engine.graphic.shape.Shape;

public class Graphics {

	private Game game;
	private Font font;

	private double globalScale, scaleX, scaleY;

	public Graphics(Game game) {
		this.game = game;
		this.font = new Font("/neuropol.ttf", 32);
		this.globalScale = 1f;
		this.scaleX = 1f;
		this.scaleY = 1f;
	}

	public void draw(Sprite sprite) {
		sprite.draw(this);
	}

	public void drawShape(Shape shape) {
        shape.draw(this);
    }

	public void drawString(String text, float x, float y, FontStyle style, Color color) {
		if (font != null)
			font.draw(text, x, y, style, color);
	}

	public void drawString(String text, float x, float y, FontStyle style) {
		drawString(text, x, y, style, Color.WHITE);
	}

	public void drawString(String text, float x, float y, Color color) {
		drawString(text, x, y, FontStyle.RIGHT, color);
	}

	public void drawString(String text, float x, float y) {
		drawString(text, x, y, FontStyle.RIGHT, Color.WHITE);
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public double getGlobalScale() {
		return globalScale;
	}

    public void setGlobalScale(double globalScale) {
        this.globalScale = globalScale;
    }

	public double getScaleX() {
		return scaleY;
	}

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

	public double getScaleY() {
		return scaleX;
	}

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

	public Game getGame() {
		return game;
	}
}

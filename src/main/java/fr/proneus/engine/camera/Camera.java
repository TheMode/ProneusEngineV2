package fr.proneus.engine.camera;

import fr.proneus.engine.Game;
import fr.proneus.engine.Game.WindowType;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.graphic.shape.Shape;

public class Camera {

	private Game game;
	private float x, y;

	public Camera(Game game) {
		this.game = game;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void move(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public Rectangle getCameraRectangle(){
		// TODO faire marcher  les methodes "visible" =(
		//float cameraWidth = !game.getWindowType().equals(WindowType.NORMAL) ? game.getVirtualWidth() : game.getWidth();
		//float cameraHeight = !game.getWindowType().equals(WindowType.NORMAL) ? game.getVirtualHeight() : game.getHeight();
		float cameraWidth = game.getVirtualWidth();
		float cameraHeight = game.getVirtualHeight();
		return new Rectangle(-this.x, -this.y, cameraWidth, cameraHeight, Color.WHITE, false);
	}

	public boolean isPartiallyVisible(float x, float y, float width, float height) {
		Rectangle camera = getCameraRectangle();
		System.out.println((camera.interact(x, y))+" "+
				(camera.interact(x + width, y))+" "+
				(camera.interact(x, y + height))+" "+
						(camera.interact(x + width, y + height)));
		return camera.interact(x, y) || camera.interact(x + width, y) || camera.interact(x, y + height)
				|| camera.interact(x + width, y + height);
	}

	public boolean isPartiallyVisible(Sprite sprite) {
		return isPartiallyVisible(sprite.x*game.getVirtualWidth(), sprite.y*game.getVirtualHeight(), sprite.getImage().getRegionPixelWidth(),
				sprite.getImage().getRegionPixelHeight());
	}

	public boolean isPartiallyVisible(Shape shape) {
		return isPartiallyVisible(shape.x, shape.y, shape.width, shape.height);
	}

	public boolean isFullyVisible(float x, float y, float width, float height) {
		Rectangle camera = getCameraRectangle();
		return camera.interact(x, y) && camera.interact(x + width, y) && camera.interact(x, y + height)
				&& camera.interact(x + width, y + height);
	}

	public boolean isFullyVisible(Sprite sprite) {
		return isFullyVisible(sprite.x, sprite.y, sprite.getImage().getImagePixelWidth(),
				sprite.getImage().getImagePixelHeight());
	}

	public boolean isFullyVisible(Shape shape) {
		return isFullyVisible(shape.x, shape.y, shape.width, shape.height);
	}

}

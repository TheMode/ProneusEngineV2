package fr.proneus.engine.graphic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fr.proneus.engine.graphic.Image.DrawType;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;

public class Sprite {

	private Image image;
	private DrawType drawType;
	public float x, y;

	public double scale;
	public double scaleX;
	public double scaleY;

	public double angle;

	public long lastAnimationDraw;

	private Map<String, Animation> animations;

	private Animation currentAnimation;
	private int currentAnimationFrame;
	private int currentAnimationSpeed;

	private Runnable animationEnd;

	public Sprite(Image image, float x, float y) {
		this.image = image;
		this.drawType = DrawType.TOP_LEFT;
		this.scale = 1;
		this.scaleX = 1;
		this.scaleY = 1;
		this.x = x;
		this.y = y;

		this.animations = new HashMap<>();
	}

	public void draw(Graphics graphic) {
		if (currentAnimation != null) {
			if (System.currentTimeMillis() - lastAnimationDraw > currentAnimationSpeed) {
				this.lastAnimationDraw = System.currentTimeMillis();
				AnimationFrame frame = currentAnimation.frames.get(currentAnimationFrame);
				this.image.regionX = frame.x;
				this.image.regionY = frame.y;
				this.image.regionWidth = frame.width;
				this.image.regionHeight = frame.height;
				if (currentAnimation.frames.get(currentAnimationFrame + 1) == null) {
					if (animationEnd != null) {
						animationEnd.run();
					}
					currentAnimationFrame = 0;
				} else {
					currentAnimationFrame++;
				}
			}
		}

		image.draw(this, graphic);
	}

	public void onAnimationEnd(Runnable animationEnd) {
		this.animationEnd = animationEnd;
	}

	public void addAnimation(String name, Animation animation) {
		this.animations.put(name, animation);
	}

	public void setAnimation(String name, int speed) {
		if (name == null) {
			currentAnimation = null;
			this.currentAnimationFrame = 0;
			this.currentAnimationSpeed = 0;
			return;
		}
		this.currentAnimation = this.animations.get(name);
		this.currentAnimationFrame = 0;
		this.currentAnimationSpeed = speed;
	}

	public String getAnimationName() {

		for (Entry<String, Animation> anim : animations.entrySet()) {
			if (anim.getValue().equals(currentAnimation))
				return anim.getKey();
		}

		return null;
	}

	public void moveFromAngle(double angle, float speedx, float speedy) {
		double rad = Math.toRadians(angle);
		float xMovement = (float) Math.cos(rad) * speedx;
		float yMovement = (float) Math.sin(rad) * speedy;

		this.x += xMovement;
		this.y += yMovement;
	}

	public void moveFromAngle(float speedx, float speedy) {
		moveFromAngle(angle, speedx, speedy);
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setDrawType(DrawType drawType) {
		this.drawType = drawType;
	}

	public DrawType getDrawType() {
		return drawType;
	}

}

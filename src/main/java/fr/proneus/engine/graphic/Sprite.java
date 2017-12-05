package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.proneus.engine.utils.Vector;

import java.util.*;
import java.util.Map.Entry;

public class Sprite {
    public double scale;
    public double scaleX;
    public double scaleY;
    public double angle;
    public long lastAnimationDraw;
    private float x, y, z;

    private Image image;
    // Force
    private List<Force> forces;
    private float forceSpeed = 0.05f;

    private Map<String, Animation> animations;

    private Animation currentAnimation;
    private int currentAnimationFrame;
    private int currentAnimationSpeed;

    private Runnable animationEnd;

    public Sprite(Image image, float x, float y) {
        this.image = image;
        this.scale = 1;
        this.scaleX = 1;
        this.scaleY = 1;
        this.x = x;
        this.y = y;

        this.forces = new ArrayList<>();

        this.animations = new HashMap<>();
    }

    public void update(Game game) {
        // Velocity
        Iterator<Force> iter = forces.iterator();
        while (iter.hasNext()) {
            Force force = iter.next();
            force.apply(this);

            if (force.finished) {
                iter.remove();
            }
        }
    }

    public void render(Graphics graphics) {

        // Animation
        if (currentAnimation != null) {
            if (System.currentTimeMillis() - lastAnimationDraw > currentAnimationSpeed) {
                this.lastAnimationDraw = System.currentTimeMillis();
                // TODO Maybe bugged ?
                AnimationFrame frame = currentAnimation.frames.get(currentAnimationFrame);
                this.image.setRegionX(frame.x);
                this.image.setRegionY(frame.y);
                this.image.setRegionWidth(frame.width);
                this.image.setRegionHeight(frame.height);
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

        image.setX(x);
        image.setY(y);
        image.setZ(z);
        image.setScale(scale);
        image.setScaleX(scaleX);
        image.setScaleY(scaleY);
        image.setAngle(angle);

        // Draw
        image.draw(graphics);
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

    public void applyForce(Vector vector, float forceSpeed) {
        this.forces.add(new Force(vector, forceSpeed));
    }

    public void applyForce(Vector vector) {
        applyForce(vector, forceSpeed);
    }

    public List<Force> getForces() {
        return forces;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public class Force {

        public Vector appliedVector;
        public Vector achievedVector;
        public float forceSpeed;
        public boolean finished;

        public boolean positiveX, positiveY;

        public Force(Vector vector, float forceSpeed) {
            this.appliedVector = vector;
            this.achievedVector = new Vector(0, 0);
            this.forceSpeed = forceSpeed;

            positiveX = appliedVector.getX() > 0;
            positiveY = appliedVector.getY() > 0;
        }

        public void apply(Sprite sprite) {
            if (!sprite.forces.contains(this)) {
                sprite.forces.add(this);
            }


            float appliedForceX = (float) appliedVector.getX() * forceSpeed;
            float appliedForceY = (float) appliedVector.getY() * forceSpeed;
            appliedForceX = positiveX ? (float) Math.min(achievedVector.getX() + appliedForceX, appliedVector.getX()) :
                    (float) Math.max(achievedVector.getX() + appliedForceX, appliedVector.getX());
            appliedForceY = positiveY ? (float) Math.min(achievedVector.getY() + appliedForceY, appliedVector.getY()) :
                    (float) Math.max(achievedVector.getY() + appliedForceY, appliedVector.getY());
            achievedVector.setX(appliedForceX);
            achievedVector.setY(appliedForceY);

            sprite.x += appliedForceX;
            sprite.y += appliedForceY;

            if (appliedVector.getX() == achievedVector.getX() && appliedVector.getY() == achievedVector.getY()) {
                finished = true;
            }
        }
    }

}

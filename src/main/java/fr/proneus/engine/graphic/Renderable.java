package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.camera.Camera;
import fr.proneus.engine.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Renderable {

    private float x, y, width, height;
    private double angle;
    private Color color;
    private DrawType drawType;

    // Image
    private boolean isImage;
    private float regionX, regionY, regionWidth, regionHeight;

    // Scale
    private double scale, scaleX, scaleY;
    private double totalScaleX, totalScaleY;

    // Force
    private List<Force> forces;
    private float defaultForceSpeed = 0.05f;

    // Camera
    private boolean zoomable;

    public Renderable(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = 1;
        this.scaleX = 1;
        this.scaleY = 1;
        this.totalScaleX = 1;
        this.totalScaleY = 1;
        this.drawType = DrawType.TOP_LEFT;

        this.isImage = (this instanceof Sprite);

        this.forces = new ArrayList<>();

        this.zoomable = true;
    }

    public void setImageValue(Image image, float regionX, float regionY, float regionWidth, float regionHeight) {
        this.regionX = regionX * image.getImagePixelWidth();
        this.regionY = regionY * image.getImagePixelHeight();
        this.regionWidth = regionWidth * image.getImagePixelWidth();
        this.regionHeight = regionHeight * image.getImagePixelHeight();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void rotate(double angle) {
        this.angle = angle;
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

    public void render(Graphics graphic) {
        // TODO support for rectangle draw type & not centered scale
        glPushMatrix();

        if (!isImage) {
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        float translateX = 0;
        float translateY = 0;
        switch (drawType) {
            case CENTERED:
                translateX = -getRegionX() - getRegionWidth() / 2;
                translateY = -getRegionY() - getRegionHeight() / 2;
                break;
            case TOP_LEFT:
                translateX = -getRegionX();
                translateY = -getRegionY();
                break;
            case TOP_RIGHT:
                translateX = -getRegionX() + getRegionWidth();
                translateY = -getRegionY();
                break;
            case DOWN_LEFT:
                translateX = -getRegionX();
                translateY = -getRegionY() + getRegionHeight();
                break;
            case DOWN_RIGHT:
                translateX = -getRegionX() + getRegionWidth();
                translateY = -getRegionY() + getRegionHeight();
                break;
        }
        glTranslatef(translateX, translateY, 0);

        if (color != null)
            glColor4f((float) color.r / 255, (float) color.g / 255, (float) color.b / 255, color.a);

        // Start center
        float x = this.x * (float) Game.getDefaultWidth();
        float y = this.y * (float) Game.getDefaultHeight();
        float centerX = isImage ? x + regionX + regionWidth / 2 : x + width * (float) Game.getDefaultWidth() / 2;
        float centerY = isImage ? y + regionY + regionHeight / 2 : y + height * (float) Game.getDefaultHeight() / 2;
        glTranslatef(centerX, centerY, 0);

        // Rotation
        if (angle != 0)
            glRotated(angle, 0, 0, 1);

        // Scale (global/sprite/Camera)
        refreshScale(graphic);
        double scaleX = getTotalScaleX();
        double scaleY = getTotalScaleY();
        if (scaleX != 1 || scaleY != 1) {
            glScaled(scaleX, scaleY, 0);
        }

        // End center
        glTranslatef(-centerX, -centerY, 0);

        render();

        glPopMatrix();
    }

    protected abstract void render();

    public abstract boolean interact(float x, float y);

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

    public float getRegionX() {
        return regionX;
    }

    public void setRegionX(float regionX) {
        this.regionX = regionX;
    }

    public float getRegionY() {
        return regionY;
    }

    public void setRegionY(float regionY) {
        this.regionY = regionY;
    }

    public float getRegionWidth() {
        return regionWidth;
    }

    public void setRegionWidth(float regionWidth) {
        this.regionWidth = regionWidth;
    }

    public float getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(float regionHeight) {
        this.regionHeight = regionHeight;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void refreshScale(Graphics graphics) {
        double resultX = Math.max(
                this.scale + (this.scaleX - 1) + (graphics.getGlobalScale() - 1) + (graphics.getScaleX() - 1), 0);
        double resultY = Math.max(
                this.scale + (this.scaleY - 1) + (graphics.getGlobalScale() - 1) + (graphics.getScaleY() - 1), 0);
        if (zoomable) {
            Camera camera = graphics.getGame().getCamera();
            resultX += camera.getZoomX() - 1;
            resultY += camera.getZoomY() - 1;
        }
        this.totalScaleX = resultX;
        this.totalScaleY = resultY;
    }

    public double getTotalScaleX() {
        return totalScaleX;
    }

    public double getTotalScaleY() {
        return totalScaleY;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScaleX() {
        return scaleX;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public DrawType getDrawType() {
        return drawType;
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }

    public float getCenterX() {
        return width / 2;
    }

    public float getCenterY() {
        return height / 2;
    }

    // Movement
    public void move(float x, float y) {
        this.setX(getX() + x);
        this.setY(getY() + y);
    }

    public void moveFromAngle(double angle, float speedx, float speedy) {
        double rad = Math.toRadians(angle);
        float xMovement = (float) Math.cos(rad) * speedx;
        float yMovement = (float) Math.sin(rad) * speedy;

        this.setX(getX() + xMovement);
        this.setY(getY() + yMovement);
    }

    public void moveFromAngle(float speedx, float speedy) {
        moveFromAngle(angle, speedx, speedy);
    }

    public List<Force> getForces() {
        return forces;
    }

    public void applyForce(Vector vector, float forceSpeed) {
        this.forces.add(new Force(vector, forceSpeed));
    }

    public void applyForce(Vector vector) {
        applyForce(vector, defaultForceSpeed);
    }

    public void applyCameraZoom(boolean zoomable) {
        this.zoomable = zoomable;
    }

    public enum DrawType {
        CENTERED, TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT
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

        public void apply(Renderable renderable) {
            if (!renderable.forces.contains(this)) {
                renderable.forces.add(this);
            }


            float appliedForceX = (float) appliedVector.getX() * forceSpeed;
            float appliedForceY = (float) appliedVector.getY() * forceSpeed;
            appliedForceX = positiveX ? (float) Math.min(achievedVector.getX() + appliedForceX, appliedVector.getX()) :
                    (float) Math.max(achievedVector.getX() + appliedForceX, appliedVector.getX());
            appliedForceY = positiveY ? (float) Math.min(achievedVector.getY() + appliedForceY, appliedVector.getY()) :
                    (float) Math.max(achievedVector.getY() + appliedForceY, appliedVector.getY());
            achievedVector.setX(appliedForceX);
            achievedVector.setY(appliedForceY);

            renderable.setX(renderable.getX() + appliedForceX);
            renderable.setY(renderable.getY() + appliedForceY);

            if (appliedVector.getX() == achievedVector.getX() && appliedVector.getY() == achievedVector.getY()) {
                finished = true;
            }
        }
    }

}

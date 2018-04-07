package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.utils.ByteBufferUtils;
import org.lwjgl.opengl.GL12;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.lwjgl.opengl.GL11.*;

public class Sprite extends Renderable {
    // Image
    private Image image;
    private int imageID;

    // Animation
    private Map<String, Animation> animations;
    private Animation currentAnimation;
    private long lastAnimationDraw;
    private int currentAnimationFrame;
    private int currentAnimationSpeed;

    // Interact rectangle
    private Rectangle interactRectangle;

    public Sprite(Image image, float x, float y) {
        super(x, y, image.getImageWidth(), image.getImageHeight());
        this.image = image;

        setImageValue(image, image.getRegionX(), image.getRegionY(), image.getRegionWidth(), image.getRegionHeight());

        this.animations = new HashMap<>();

        this.interactRectangle = new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void update(Game game) {
        super.update(game);
        // Animation
        if (currentAnimation != null) {
            if (System.currentTimeMillis() - lastAnimationDraw > currentAnimationSpeed) {
                this.lastAnimationDraw = System.currentTimeMillis();
                AnimationFrame frame = currentAnimation.frames.get(currentAnimationFrame);
                this.setRegionX(frame.x);
                this.setRegionY(frame.y);
                this.setRegionWidth(frame.width);
                this.setRegionHeight(frame.height);
                onAnimationFrame(getAnimationName(), currentAnimationFrame);
                if (currentAnimation.frames.get(currentAnimationFrame + 1) == null) {
                    onAnimationEnd(getAnimationName());
                    currentAnimationFrame = 0;
                } else {
                    currentAnimationFrame++;
                }
            }
        }
    }

    @Override
    public void render() {
        if (!isImageLoaded())
            imageID = loadTexture(image.getBufferedImage());

        glBindTexture(GL_TEXTURE_2D, imageID);

        float regionX = getRegionX();
        float regionY = getRegionY();
        float regionWidth = getRegionWidth();
        float regionHeight = getRegionHeight();

        float dcx = regionX / (float) image.getImagePixelWidth();
        float dcy = regionY / (float) image.getImagePixelHeight();

        float dcw = (regionX + regionWidth) / (float) image.getImagePixelWidth();
        float dch = (regionY + regionHeight) / (float) image.getImagePixelHeight();

        float x = getX() * (float) Game.getCameraWidth();
        float y = getY() * (float) Game.getCameraHeight();

        glBegin(GL_QUADS);

        glTexCoord2f(dcx, dcy);
        glVertex2f((regionX) + x, (regionY) + y);
        glTexCoord2f(dcw, dcy);
        glVertex2f((regionX + regionWidth) + x, (regionY) + y);
        glTexCoord2f(dcw, dch);
        glVertex2f((regionX + regionWidth) + x, (regionY + regionHeight) + y);
        glTexCoord2f(dcx, dch);
        glVertex2f((regionX) + x, (regionY + regionHeight) + y);

        glEnd();
    }

    private int loadTexture(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = ByteBufferUtils.convertImage(image);

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                buffer);

        return textureID;
    }

    public void onAnimationEnd(String name) {
    }

    public void onAnimationFrame(String name, int frame) {
    }

    public void addAnimation(String name, Animation animation) {
        this.animations.put(name, animation);
    }

    public void setAnimation(String name, int speed) {
        if (name == null) {
            this.currentAnimation = null;
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

    @Override
    public boolean interact(float x, float y) {
        // TODO check if is working ?
        this.interactRectangle.setX(getRegionX() / image.getImagePixelWidth());
        this.interactRectangle.setY(getRegionY() / image.getImagePixelHeight());
        this.interactRectangle.setWidth(getRegionWidth() / image.getImagePixelWidth());
        this.interactRectangle.setHeight(getRegionHeight() / image.getImagePixelHeight());
        return this.interactRectangle.interact(x, y);
    }

    public boolean overlaps(Rectangle r) {
        return getX() < r.getX() + r.getWidth() && getX() + getWidth() > r.getX() && getY() < r.getY() + r.getHeight() && getY() + getHeight() > r.getY();
    }

    // Image settings
    public int getImageID() {
        return imageID;
    }

    // Image is loaded during first draw
    public boolean isImageLoaded() {
        return getImageID() != 0;
    }

    public Image getImage() {
        return image;
    }

    // The new image will have to be loaded
    public void setImage(Image image) {
        if (!this.image.equals(image)) {
            imageID = 0;
        }
        this.image = image;
    }

}

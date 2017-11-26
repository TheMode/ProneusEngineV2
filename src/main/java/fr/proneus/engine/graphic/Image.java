package fr.proneus.engine.graphic;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import fr.proneus.engine.Game;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class Image {

    private BufferedImage image;
    private String path;
    private int imageWidth, imageHeight;
    private float regionX, regionY, regionWidth, regionHeight;
    private int texID;

    private Color color;

    public Image(String path, float x, float y, float width, float height) {
        try {
            this.image = ImageIO.read(Game.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        this.regionX = x;
        this.regionY = y;
        this.regionWidth = width;
        this.regionHeight = height;
    }

    public Image(String path) {
        try {
            this.image = ImageIO.read(Game.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.path = path;

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        this.regionX = 0;
        this.regionY = 0;
        this.regionWidth = 1;
        this.regionHeight = 1;
    }

    public void draw(Sprite sprite, Graphics graphic) {
        if (!isLoaded())
            texID = loadTexture(image);

        float regionX = getRegionPixelX();
        float regionY = getRegionPixelY();

        float regionWidth = getRegionPixelWidth();
        float regionHeight = getRegionPixelHeight();

        float imageWidth = getImagePixelWidth();
        float imageHeight = getImagePixelHeight();

        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, texID);

        float translateX = 0;
        float translateY = 0;
        switch (sprite.getDrawType()) {
            case CENTERED:
                translateX = -regionX - regionWidth / 2;
                translateY = -regionY - regionHeight / 2;
                break;
            case TOP_LEFT:
                translateX = -regionX;
                translateY = -regionY;
                break;
            case TOP_RIGHT:
                translateX = -regionX + regionWidth;
                translateY = -regionY;
                break;
            case DOWN_LEFT:
                translateX = -regionX;
                translateY = -regionY + regionHeight;
                break;
            case DOWN_RIGHT:
                translateX = -regionX + regionWidth;
                translateY = -regionY + regionHeight;
                break;
        }

        glTranslatef(translateX, translateY, 0);
        if (color != null)
            glColor4f((float) color.r / 255, (float) color.g / 255, (float) color.b / 255, color.a);

        // Transformation
        float x = sprite.x * 1920f;
        float y = sprite.y * 1080f;
        glTranslatef(x + regionX + regionWidth / 2, y + regionY + regionHeight / 2, 0);
        //System.out.println("2: "+(x+regionX + regionWidth / 2));
        //System.out.println("3: "+(y + regionY + regionHeight / 2));

        // Rotation
        if (sprite.angle != 0)
            glRotated(sprite.angle, 0, 0, 1);

        // Scale
        double scaleX = Math.max(
                sprite.scale + (sprite.scaleX - 1) + (graphic.getGlobalScale() - 1) + (graphic.getScaleX() - 1), 0);
        double scaleY = Math.max(
                sprite.scale + (sprite.scaleY - 1) + (graphic.getGlobalScale() - 1) + (graphic.getScaleY() - 1), 0);
        if (scaleX != 1 || scaleY != 1) {
            glScaled(scaleX, scaleY, 1);
        }

        glTranslatef(-(x + regionX + regionWidth / 2), -(y + regionY + regionHeight / 2), 0);

        float dcx = regionX / imageWidth;
        float dcy = regionY / imageHeight;

        float dcw = (regionX + regionWidth) / imageWidth;
        float dch = (regionY + regionHeight) / imageHeight;

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

        glPopMatrix();
    }

    protected int loadTexture(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // G
                buffer.put((byte) (pixel & 0xFF)); // B
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // A

            }
        }

        buffer.flip();

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

    public float getCenterX() {
        return regionWidth / 2;
    }

    public float getCenterY() {
        return regionHeight / 2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTextureID() {
        return texID;
    }

    public boolean isLoaded() {
        return this.texID != 0;
    }

    public BufferedImage getBufferedImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    public float getImageWidth() {
        return imageWidth / 1920f;
    }

    public int getImagePixelWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight / 1080f;
    }

    public int getImagePixelHeight() {
        return imageHeight;
    }

    public float getRegionPixelX() {
        return regionX * (float) imageWidth;
    }

    public float getRegionPixelY() {
        return regionY * (float) imageHeight;
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

    public float getRegionPixelWidth() {
        return regionWidth * getImagePixelWidth();
    }

    public float getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(float regionHeight) {
        this.regionHeight = regionHeight;
    }

    public float getRegionPixelHeight() {
        return regionHeight * getImagePixelHeight();
    }

    public enum DrawType {
        CENTERED, TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT;
    }

}

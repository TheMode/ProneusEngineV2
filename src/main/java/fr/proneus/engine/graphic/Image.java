package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Image extends Renderable {

    private BufferedImage image;
    private String path;
    private int imageWidth, imageHeight;
    private int texID;

    private Color color;

    public Image(String path, float x, float y, float width, float height) {
        super(0, 0, 0, 0);
        try {
            this.image = ImageIO.read(FileUtils.getInternalFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        setImageValue(this, x, y, width, height);
    }

    public Image(String path) {
        this(path, 0, 0, 1, 1);
    }

    public Image(InputStream inputStream, float x, float y, float width, float height) {
        super(x, y, width, height);
        try {
            this.image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = "";

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();

        setImageValue(this, x, y, width, height);
    }

    public Image(InputStream inputStream) {
        this(inputStream, 0, 0, 1, 1);
    }

    @Override
    public void render() {
        if (!isLoaded())
            texID = loadTexture(image);

        glBindTexture(GL_TEXTURE_2D, texID);

        float regionX = getRegionX();
        float regionY = getRegionY();
        float regionWidth = getRegionWidth();
        float regionHeight = getRegionHeight();

        float dcx = regionX / imageWidth;
        float dcy = regionY / imageHeight;

        float dcw = (regionX + regionWidth) / imageWidth;
        float dch = (regionY + regionHeight) / imageHeight;

        float x = getX() * (float) Game.getDefaultWidth();
        float y = getY() * (float) Game.getDefaultHeight();

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

    @Override
    public boolean interact(float x, float y) {
        // TODO optimize ?
        return new Rectangle(getX(), getY(), getWidth(), getHeight()).interact(x, y);
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
        return imageWidth / (float) Game.getDefaultWidth();
    }

    public int getImagePixelWidth() {
        return imageWidth;
    }

    public float getImageHeight() {
        return imageHeight / (float) Game.getDefaultHeight();
    }

    public int getImagePixelHeight() {
        return imageHeight;
    }


    /*public float getRegionX() {
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
    public float getRegionPixelHeight() {
        return regionHeight * getImagePixelHeight();
    }*/

}

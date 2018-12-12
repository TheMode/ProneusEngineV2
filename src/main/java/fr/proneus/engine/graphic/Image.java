package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Image {

    private BufferedImage image;
    private String path;
    private int imageWidth, imageHeight;

    public Image(File file, float x, float y, float width, float height) {
        try {
            this.image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = path;

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
    }

    public Image(File file) {
        this(file, 0, 0, 1, 1);
    }

    public Image(InputStream inputStream, float x, float y, float width, float height) {
        try {
            this.image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.path = "";

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
    }

    public Image(InputStream inputStream) {
        this(inputStream, 0, 0, 1, 1);
    }

    public BufferedImage getBufferedImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    public float getImageWidth() {
        return imageWidth / (float) Game.getCameraWidth();
    }

    public float getImageHeight() {
        return imageHeight / (float) Game.getCameraHeight();
    }

    public int getImagePixelWidth() {
        return imageWidth;
    }

    public int getImagePixelHeight() {
        return imageHeight;
    }

}

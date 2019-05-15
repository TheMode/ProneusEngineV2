package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Image {

    private BufferedImage image;
    private int imageWidth, imageHeight;

    public Image(InputStream inputStream, float x, float y, float width, float height) {
        try {
            this.image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.imageWidth = image.getWidth();
        this.imageHeight = image.getHeight();
    }

    public Image(InputStream inputStream) {
        this(inputStream, 0, 0, 1, 1);
    }

    public Image(File file, float x, float y, float width, float height) {
        this(toInputStream(file), x, y, width, height);
    }

    public Image(File file) {
        this(file, 0, 0, 1, 1);
    }

    public BufferedImage getBufferedImage() {
        return image;
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

    private static InputStream toInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("File " + file.getPath() + " do not exist.");
            return null;
        }
    }

}

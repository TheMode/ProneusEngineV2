package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Image {

    private BufferedImage image;
    private String path;
    private int imageWidth, imageHeight;
    private float regionX, regionY, regionWidth, regionHeight;

    public Image(String path, float x, float y, float width, float height) {
        try {
            this.image = ImageIO.read(FileUtils.getInternalFile(path));
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
        this(path, 0, 0, 1, 1);
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

        this.regionX = x;
        this.regionY = y;
        this.regionWidth = width;
        this.regionHeight = height;
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
        return imageWidth / (float) Game.getDefaultWidth();
    }

    public float getImageHeight() {
        return imageHeight / (float) Game.getDefaultHeight();
    }

    public int getImagePixelWidth() {
        return imageWidth;
    }

    public int getImagePixelHeight() {
        return imageHeight;
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

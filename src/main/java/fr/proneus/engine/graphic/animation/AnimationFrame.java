package fr.proneus.engine.graphic.animation;

import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Texture;

public class AnimationFrame {

    public Texture texture;
    public float x, y, width, height;

    public int columnsNumber, rowsNumber;

    public AnimationFrame(Texture texture, int columns, int rows, int columnsNumber, int rowsNumber) {
        this.texture = texture;

        Image image = texture.getImage();
        float pixelWidth = (float) image.getImagePixelWidth();
        float pixelHeight = (float) image.getImagePixelHeight();

        float spriteX = pixelWidth / (float) columnsNumber;
        float spriteY = pixelHeight / (float) rowsNumber;

        this.x = spriteX * (float) columns / pixelWidth;
        this.y = spriteY * rows / pixelHeight;
        this.width = spriteX / pixelWidth;
        this.height = spriteY / pixelHeight;

        this.columnsNumber = columnsNumber;
        this.rowsNumber = rowsNumber;
    }


}

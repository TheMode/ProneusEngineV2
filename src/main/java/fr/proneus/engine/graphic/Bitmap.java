package fr.proneus.engine.graphic;

public class Bitmap {

    private int width, height;

    private int[] pixels;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public Bitmap(int width, int height, int[] textureData) {
        this.width = width;
        this.height = height;
        this.pixels = textureData;
    }

    public void setPixel(int x, int y, float r, float g, float b, float a) {

        r *= 255;
        g *= 255;
        b *= 255;
        a *= 255;

        int location = x * height + y;

        int rgba = ((int) a << 24) + ((int) r << 16) + ((int) g << 8) + ((int) b);

        pixels[location] = rgba;
    }

    public void setPixel(int x, int y, Color color) {
        setPixel(x, y, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

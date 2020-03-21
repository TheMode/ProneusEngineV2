package fr.proneus.engine.graphic;

public class Color {

    public static final Color BLACK = new Color(0, 0, 0);

    public static final Color WHITE = new Color(1, 1, 1);

    public static final Color SILVER = new Color(0.75f, 0.75f, 0.75f);

    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f);

    public static final Color DARK_GRAY = new Color(0.37f, 0.37f, 0.37f);

    public static final Color RED = new Color(1, 0, 0);

    public static final Color DARK_RED = new Color(0.54f, 0, 0);

    public static final Color MAROON = new Color(0.5f, 0, 0);

    public static final Color YELLOW = new Color(1, 1, 0);

    public static final Color GOLD = new Color(1, 0.7f, 0);

    public static final Color GREEN = new Color(0, 1, 0);

    public static final Color DARK_GREEN = new Color(0, 0.5f, 0);

    public static final Color AQUA = new Color(0, 1, 1);

    public static final Color BLUE = new Color(0, 0, 1);

    public static final Color PINK = new Color(1, 0, 1);

    public static final Color PURPLE = new Color(0.5f, 0, 0.5f);

    private float r, g, b;
    private float a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public float getAlpha() {
        return a;
    }
}

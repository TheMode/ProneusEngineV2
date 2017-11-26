package fr.proneus.engine.graphic;

public class Color {
	
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color SILVER = new Color(192, 192, 192);
	public static final Color GRAY = new Color(128, 128, 128);
	public static final Color DARK_GRAY = new Color(96, 96, 96);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color DARK_RED = new Color(139, 0, 0);
	public static final Color MAROON = new Color(128, 0, 0);
	public static final Color YELLOW = new Color(255, 255, 0);
	public static final Color GOLD = new Color(255, 180, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color DARK_GREEN = new Color(0, 128, 0);
	public static final Color AQUA = new Color(0, 255, 255);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color PINK = new Color(255, 0, 255);
	public static final Color PURPLE = new Color(128, 0, 128);
	
	public int r, g, b;
	public float a;
	
	public Color(int r, int g, int b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(int r, int g, int b){
		this(r, g, b, 1);
	}

}

package fr.proneus.engine;

public enum Cursor {
	
	ARROW_CURSOR(0x36001),
	IBEAM_CURSOR(0x36002),
	CROSSHAIR_CURSOR(0x36003),
	HAND_CURSOR(0x36004),
	HRESIZE_CURSOR(0x36005),
	VRESIZE_CURSOR(0x36006);
	
	private int value;
	
	private Cursor(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

}

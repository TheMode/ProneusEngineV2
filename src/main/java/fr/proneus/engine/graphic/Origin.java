package fr.proneus.engine.graphic;

public enum Origin {

	// Default origin is CENTER
	// x += width * origin.widthModifier
	// y += height * origin.heightModifier

	TOP_LEFT(-0.5f, -0.5f), TOP_CENTER(0, -0.5f), TOP_RIGHT(0.5f, -0.5f),

	CENTER_LEFT(-0.5f, 0), CENTER(0, 0), CENTER_RIGHT(0.5f, 0),

	BOTTOM_LEFT(-0.5f, 0.5f), BOTTOM_CENTER(0, 0.5f), BOTTOM_RIGHT(0.5f, 0.5f);

	private float widthModifier;
	private float heightModifier;

	private Origin(float widthModifier, float heightModifier) {
		this.widthModifier = widthModifier;
		this.heightModifier = heightModifier;
	}

	public float getWidthModifier() {
		return widthModifier;
	}

	public float getHeightModifier() {
		return heightModifier;
	}

}

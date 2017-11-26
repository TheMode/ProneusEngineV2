package fr.proneus.engine.graphic.animation;

import fr.proneus.engine.graphic.Image;

public class AnimationFrame {
	
	public float x, y, width, height;
	
	public AnimationFrame(Image image, int columns, int rows, int columnsNumber, int rowsNumber) {
		int spriteX = image.getImagePixelWidth()/columnsNumber;
		int spriteY = image.getImagePixelHeight()/rowsNumber;
		this.x = spriteX*columns;
		this.y = spriteY*rows;
		this.width = spriteX;
		this.height = spriteY;
	}
	
	

}

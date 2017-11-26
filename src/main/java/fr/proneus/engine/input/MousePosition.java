package fr.proneus.engine.input;

import fr.proneus.engine.Game;

public class MousePosition {
	
	private Game game;
	private int x, y;
	
	public MousePosition(Game game, int x, int y) {
		this.game = game;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public MousePosition toCameraPosition(){
		return new MousePosition(game, x-(int)game.getCamera().getX(), y-(int)game.getCamera().getY());
	}
	
	

}

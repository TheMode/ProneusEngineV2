package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Font.FontStyle;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;

public abstract class Button extends Component {

	private Rectangle rect;
	private Rectangle border;
	
	private String text;
	private boolean hover;
	
	public Button(String text, float x, float y, float width, float height) {
		this.rect = new Rectangle(x, y, width, height, Color.GRAY, true);
        this.border = new Rectangle(x, y - 0.0002f, width + 2, height + 0.0002f, Color.DARK_GRAY, true);
        this.text = text;
	}

	@Override
	public void update(Game game) {
        MousePosition mouse = game.getInput().getMousePosition().toCameraPosition();
        System.out.println(mouse.getX()+" "+mouse.getY());
		this.hover = this.rect.interact(mouse.getX(), mouse.getY());
		
		this.rect.setColor(this.hover ? new Color(64, 64, 64) : Color.GRAY);
	}

	@Override
	public void render(Game game, Graphics graphic) {
		graphic.drawShape(border);
		graphic.drawShape(rect);
		graphic.drawString(text, rect.x+rect.width/2, rect.y+rect.height/2, FontStyle.CENTERED, Color.WHITE);
	}
	
	@Override
	public void onMouseDown(Game game, int key) {
		if(hover)
			onClick(key);
	}
	
	public abstract void onClick(int key);

}

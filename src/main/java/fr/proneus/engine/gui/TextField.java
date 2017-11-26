package fr.proneus.engine.gui;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Font;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.MousePosition;

public class TextField extends Component {

	public final static String DEFAULT_ACCEPTED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_ ";

	private float x, y, width, height;

	private String text;
	private String drawText;
	private Rectangle textField;

	private boolean password;
	private char passwordChar;

	private int view;

	private int separator;
	private boolean focus;

	private Font font;

	private String acceptedString;
	private int length;

	public TextField(float x, float y, float width, float height, Font font, String acceptedString, int length) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = "";
		this.drawText = "";
		this.separator = 0;

		this.textField = new Rectangle(x, y, width, height, Color.DARK_GRAY);
		this.font = font;

		this.acceptedString = acceptedString;
		this.length = length;
	}

	public TextField(float x, float y, float width, float height, Font font) {
		this(x, y, width, height, font, DEFAULT_ACCEPTED, 255);
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void render(Game game, Graphics graphic) {
		if (!graphic.getFont().equals(font)) {
			graphic.setFont(font);
		}
		graphic.drawShape(textField);
		graphic.drawString(drawText, x, y + height);

		if (focus) {
			float x = this.x;
			try {
				drawText.substring(0, separator);
				x += font.getWidth(getTextWidth(drawText, 0, separator));

				if (text.length() > separator) {
					boolean isSpace = text.substring(separator - 1, separator).equals(" ");
					x += isSpace ? 8 : 0;
				}
			} catch (StringIndexOutOfBoundsException e) {
				x += font.getWidth(drawText);
			}
			if (separator > 0){
				graphic.drawShape(new Rectangle(x, y - height + height, 3, height));
			}else{
				graphic.drawShape(new Rectangle(this.x, y - height + height, 3, height));
			}
		}

	}

	private void onPress(Game game, int key, int scancode) {
		if (!focus)
			return;
		String keyString = game.getInput().getKeyReturn(key, scancode);
		if (keyString != null && !(text.length() >= length)
				&& acceptedString.toLowerCase().contains(keyString.toLowerCase())) {
			text = text.substring(0, separator) + keyString + text.substring(separator, text.length());
			separator++;
		}
		switch (key) {
		case GLFW_KEY_BACKSPACE:
			if (text.length() > 0 && text.length() > separator - 1) {
				if (separator == 0)
					break;
				if (text.length() == 1) {
					text = "";
				} else {
					text = text.substring(0, separator - 1) + text.substring(separator);
				}
				separator--;
			}
			break;

		case GLFW_KEY_LEFT:
			if (text.length() != 0 && separator - 1 >= 0) {
				separator--;
			}

			break;

		case GLFW_KEY_RIGHT:
			if (text.length() != 0 && separator < text.length()) {
				separator++;
			}
			break;

		default:
			break;
		}

		if (font.getWidth(getTextWidth(text, 0, separator)) >= width) {
			for (int i = 0; i < text.length(); i++) {
				if (font.getWidth(getTextWidth(text, 0, separator).substring(i)) < width) {
					this.view = i;
					break;
				}
			}
		} else {
			view = 0;
		}

		this.drawText = text.length() > 0 ? text.substring(view) : "";

		if (password) {
			String passwordText = "";
			for (int i = 0; i < drawText.length(); i++) {
				passwordText += passwordChar;
			}
			this.drawText = passwordText;
		}

		while (font.getWidth(drawText) >= width) {
			drawText = drawText.substring(0, drawText.length() - 1);
		}

	}

	private String getTextWidth(String text, int begin, int end) {
		if (this.password) {
			String passwordString = "";
			for (int i = 0; i < text.length(); i++) {
				passwordString += passwordChar;
			}
			text = passwordString;
		}

		return text.substring(begin, end);
	}

	@Override
	public void onKeyDown(Game game, int key, int scancode) {
		onPress(game, key, scancode);
	}

	@Override
	public void onKeyRepeat(Game game, int key, int scancode) {
		onPress(game, key, scancode);
	}

	@Override
	public void onMouseDown(Game game, int key) {
		if (key == 0) {
			MousePosition mouse = game.getInput().getVirtualMousePosition().toCameraPosition();
			this.focus = textField.interact(mouse.getX(), mouse.getY());
		}
	}

	public void allowSpace(boolean space) {
		if (!space) {
			this.acceptedString = this.acceptedString.replace(" ", "");
		}
	}

	public void setPassword(char password) {
		this.password = true;
		this.passwordChar = password;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text.length() <= length) {
			this.text = text;
		} else {
			throw new IllegalArgumentException("Max text length is " + length);
		}
	}

}

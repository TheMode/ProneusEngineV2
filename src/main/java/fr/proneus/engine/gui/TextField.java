package fr.proneus.engine.gui;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Font;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.input.Keys;
import fr.proneus.engine.input.MousePosition;

public class TextField extends Component {

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

    private String forbiddenCharacters;
    private int length;

    public TextField(float x, float y, float width, float height, Font font, String forbiddenCharacters, int length) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = "";
        this.drawText = "";
        this.separator = 0;

        this.textField = new Rectangle(x, y, width, height, Color.DARK_GRAY, true);
        this.textField.applyCameraZoom(false);
        this.font = font;

        this.forbiddenCharacters = forbiddenCharacters;
        this.length = length;
    }

    public TextField(float x, float y, float width, float height, Font font) {
        this(x, y, width, height, font, "", 255);
    }

    @Override
    public void update(Game game) {
        if (game.getInput().isMouseJustDown(0)) {
            MousePosition mouse = game.getInput().getMousePosition().toCameraPosition();
            this.focus = textField.interact(mouse.getX(), mouse.getY());
        }
    }

    @Override
    public void render(Game game, Graphics graphic) {
        if (!graphic.getFont().equals(font)) {
            graphic.setFont(font);
        }
        float textHeight = 0.01f;
        graphic.drawString(drawText, x, y + height - textHeight);

        if (focus) {
            float x = this.x;
            try {
                drawText.substring(0, separator);
                x += font.getWidth(getTextWidth(drawText, 0, separator));

                String rightChar = text.substring(separator - 1, separator);
                if (rightChar != null && !forbiddenCharacters.contains(" ")) {
                    // Space add
                    boolean isSpace = rightChar.equals(" ");
                    x += isSpace ? 8 / (float) Game.getDefaultWidth() : 0;
                }
            } catch (StringIndexOutOfBoundsException e) {
                x += font.getWidth(drawText);
            }
            // TODO camera zoom false
            if (separator > 0) {
                graphic.draw(new Rectangle(x, y - height + height, 0.001f, height));
            } else {
                graphic.draw(new Rectangle(this.x, y - height + height, 0.001f, height));
            }
        }
    }

    private void refresh() {
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

    private void onCharPress(Game game, char character) {
        if (!focus)
            return;
        String keyString = String.valueOf(character);
        if (keyString != null && !(text.length() >= length)
                && !forbiddenCharacters.toLowerCase().contains(keyString.toLowerCase())) {
            text = text.substring(0, separator) + keyString + text.substring(separator, text.length());
            separator++;
        }

        refresh();

    }

    private void onKeyPress(Game game, int key) {
        if (!focus)
            return;
        switch (key) {
            case Keys.BACKSPACE:
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

            case Keys.LEFT:
                if (text.length() != 0 && separator - 1 >= 0) {
                    separator--;
                }

                break;

            case Keys.RIGHT:
                if (text.length() != 0 && separator < text.length()) {
                    separator++;
                }
                break;

            default:
                break;
        }

        refresh();
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
    public void onCharCallback(Game game, char character) {
        onCharPress(game, character);
    }

    @Override
    public void onKeyDown(Game game, int key, int scancode) {
        onKeyPress(game, key);
    }

    @Override
    public void onKeyRepeat(Game game, int key, int scancode) {
        onKeyPress(game, key);
    }

    public void allowSpace(boolean space) {
        if (!space) {
            this.forbiddenCharacters += " ";
        } else {
            this.forbiddenCharacters = this.forbiddenCharacters.replace(" ", "");
        }
    }

    public void setPassword(char password) {
        this.password = true;
        this.passwordChar = password;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

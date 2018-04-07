package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.gui.ImageButton;
import fr.proneus.engine.gui.TextField;
import fr.proneus.engine.state.State;

public class TestGUI extends State {

    private Sprite background;

    private TextField username;
    private TextField password;

    private Rectangle usernameRectangle;
    private Rectangle passwordRectangle;

    private ImageButton button;
    private Image buttonImage, pressedButtonImage;

    private boolean isLogged;

    @Override
    public void create(Game game) {

        this.background = new Sprite(new Image("/background/chicago.jpg"), 0f, 0f);

        this.buttonImage = new Image("/HUD/button_default.png");
        this.pressedButtonImage = new Image("/HUD/button_pressed.png");

        username = new TextField(0.5f - 0.1f, 0.1f, 0.2f, 0.05f, game.getCurrentFont());
        usernameRectangle = this.createRenderable(new Rectangle(0.5f - 0.1f, 0.1f, 0.2f, 0.05f, Color.DARK_GRAY, true));
        usernameRectangle.applyCameraZoom(false);

        password = new TextField(0.5f - 0.1f, 0.3f, 0.2f, 0.05f, game.getCurrentFont());
        password.setPassword('*');
        password.allowSpace(false);
        passwordRectangle = this.createRenderable(new Rectangle(0.5f - 0.1f, 0.3f, 0.2f, 0.05f, Color.DARK_GRAY, true));
        passwordRectangle.applyCameraZoom(false);

        button = new ImageButton(0.5f - 0.1f, 0.5f, buttonImage, buttonImage, pressedButtonImage) {

            @Override
            public void onClick() {
                isLogged = true;
                System.out.println(username.getText() + " : " + password.getText());

            }
        };

        addComponent(username);
        addComponent(password);
        addComponent(button);
    }

    @Override
    public void update(Game game) {
    }

    @Override
    public void render(Game game, Graphics graphic) {
        graphic.draw(background);
        graphic.draw(usernameRectangle);
        graphic.draw(passwordRectangle);
        //graphic.drawString("Username: ", game.getVirtualWidth() / 2 - 100, 300 + 35, FontStyle.LEFT, Color.WHITE);
        //graphic.drawString("Password: ", game.getVirtualWidth() / 2 - 100, 400 + 35, FontStyle.LEFT, Color.WHITE);

        if (isLogged)
            graphic.drawString("Connexion en cours...",
                    0.5f - graphic.getFont().getWidth("Connexion en cours...") / 2, 0.2f,
                    Color.GREEN);
    }

    @Override
    public void exit(Game game) {

    }

}

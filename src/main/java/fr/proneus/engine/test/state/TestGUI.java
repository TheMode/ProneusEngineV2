package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.shape.Rectangle;
import fr.proneus.engine.gui.Button;
import fr.proneus.engine.gui.TextField;
import fr.proneus.engine.state.State;

public class TestGUI extends State {

    private TextField username;
    private TextField password;

    private Button button;

    private boolean isLogged;

    @Override
    public void create(Game game) {
        username = new TextField(0.5f - 0.1f, 0.1f, 0.2f, 0.05f, game.getCurrentFont());

        password = new TextField(0.5f - 0.1f, 0.3f, 0.2f, 0.05f, game.getCurrentFont());
        password.setPassword('*');

        button = new Button("Connexion", 0.5f - 0.1f, 0.5f, 0.2f, 0.1f) {

            @Override
            public void onClick(int key) {
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
        graphic.draw(new Rectangle(0f, 0, 1, 1, Color.RED, true));
        //graphic.drawString("Username: ", game.getVirtualWidth() / 2 - 100, 300 + 35, FontStyle.LEFT, Color.WHITE);
        //graphic.drawString("Password: ", game.getVirtualWidth() / 2 - 100, 400 + 35, FontStyle.LEFT, Color.WHITE);

        // Test performance
        int performance = 10000;
        for (int i = 0; i < performance; i++) {
            graphic.draw(new Rectangle(0, 0, 1, 1));
        }


        if (isLogged)
            graphic.drawString("Connexion en cours...",
                    0.5f - graphic.getFont().getWidth("Connexion en cours...") / 2, 0.2f,
                    Color.GREEN);
    }

    @Override
    public void exit(Game game) {

    }

}

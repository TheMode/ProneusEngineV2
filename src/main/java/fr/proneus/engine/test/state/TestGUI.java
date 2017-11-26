package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Font.FontStyle;
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
		username = new TextField(game.getVirtualWidth() / 2 - 100, 300, 200, 35, game.getCurrentFont());

		password = new TextField(game.getVirtualWidth() / 2 - 100, 400, 200, 35, game.getCurrentFont());
		password.setPassword('*');

		button = new Button("Connexion", game.getVirtualWidth() / 2 - 100, 500, 200, 100) {

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
		graphic.drawShape(new Rectangle(0, 0, game.getVirtualWidth(), game.getVirtualHeight(), Color.RED, true));
		graphic.drawString("Username: ", game.getVirtualWidth() / 2 - 100, 300 + 35, FontStyle.LEFT, Color.WHITE);
		graphic.drawString("Password: ", game.getVirtualWidth() / 2 - 100, 400 + 35, FontStyle.LEFT, Color.WHITE);

		if (isLogged)
			graphic.drawString("Connexion en cours...",
					game.getVirtualWidth() / 2 - graphic.getFont().getWidth("Connexion en cours...") / 2, 200,
					Color.GREEN);
	}

	@Override
	public void exit(Game game) {

	}

}

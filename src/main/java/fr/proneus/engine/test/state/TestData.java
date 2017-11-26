package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.data.Data;
import fr.proneus.engine.data.DataFile;
import fr.proneus.engine.data.DataManager;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.state.State;

public class TestData extends State {

	private DataFile file;

	@Override
	public void create(Game game) {
		DataManager dataManager = game.getDataManager();
		this.file = dataManager.getFile("test");
		file.setData("data0", 50);
		file.setData("TEST", "stringTest");
		Data<Integer> data = file.getData("data0");

		System.out.println(data.get() + 5);
		System.out.println("Data: " + data.get());
	}

	@Override
	public void update(Game game) {

	}

	@Override
	public void render(Game game, Graphics graphic) {

	}

	@Override
	public void exit(Game game) {

	}

}

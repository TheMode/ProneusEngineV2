package fr.proneus.engine.test.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.filedialog.FileDialog;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.state.State;

public class TestFileChooser extends State {

    // TODO

    @Override
    public void create(Game game) {
        Color color = FileDialog.createColorChooserDialog("TITRE", Color.GOLD).getColor();
        if (color != null) {
            System.out.println(color.r + " " + color.g + " " + color.b);
        }
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

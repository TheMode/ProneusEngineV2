package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.filedialog.FileDialog;
import fr.proneus.engine.graphic.Graphics;

public class FileDialogState extends State {
    @Override
    public void create() {
        FileDialog file = FileDialog.openSingle("png,jpeg;pdf", null);
        if (file.getResult() == FileDialog.FileDialogResult.SUCCESS) {
            System.out.println(file.getPath());
        }else{
            System.out.println("No file");
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void exit() {

    }
}

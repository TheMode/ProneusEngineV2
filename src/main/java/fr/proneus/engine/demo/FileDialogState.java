package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.filedialog.FileDialog;

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
    public void render() {

    }

    @Override
    public void exit() {

    }
}

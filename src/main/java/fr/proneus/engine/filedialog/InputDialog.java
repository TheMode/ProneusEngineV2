package fr.proneus.engine.filedialog;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.tinyfd_inputBox;

public class InputDialog {

    private String input;

    protected InputDialog(String title, String message, String defaultMessage) {
        input = tinyfd_inputBox(title, message, defaultMessage);
        System.out.println("new: "+input);
    }

    public String getInput() {
        return input;
    }
}

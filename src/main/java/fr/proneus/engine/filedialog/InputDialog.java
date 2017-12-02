package fr.proneus.engine.filedialog;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class InputDialog {

    private String input;

    protected InputDialog(String title, String message, String defaultMessage) {
        input = tinyfd_inputBox(title, message, defaultMessage);
    }

    public String getInput() {
        return input;
    }
}

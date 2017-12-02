package fr.proneus.engine.filedialog;

import org.lwjgl.system.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class FileChooserDialog {

    private String path;

    protected FileChooserDialog(String title, String defaultPath, FileDialog.FileFilter fileFilter, String description, boolean multipleSelect) {
        try (MemoryStack stack = stackPush()) {
            path = tinyfd_openFileDialog(title, defaultPath, fileFilter.create(stack), description, multipleSelect);
        }
    }

    public String getPath() {
        return path;
    }
}
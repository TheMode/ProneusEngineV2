package fr.proneus.engine.filedialog;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class FileSaveDialog {

    private String path;

    protected FileSaveDialog(String title, String defaultPath, FileDialog.FileFilter fileFilter, String description) {
        try (MemoryStack stack = stackPush()) {
            path = tinyfd_saveFileDialog(title, defaultPath, fileFilter.create(stack), description);
        }
    }

    public String getPath() {
        return path;
    }
}

package fr.proneus.engine.filedialog;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class FolderSelectDialog {

    private String path;

    protected FolderSelectDialog(String title, String defaultPath) {
        try (MemoryStack stack = stackPush()) {
            path = tinyfd_selectFolderDialog(title, defaultPath);
        }
    }

    public String getPath() {
        return path;
    }
}

package fr.proneus.engine.filedialog;

import fr.proneus.engine.graphic.Color;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class FileDialog {

    public static void beepSound() {
        tinyfd_beep();
    }

    public static NotificationDialog createNotificationDialog(String title, String message, NotificationDialog.NotificationIcon icon) {
        return new NotificationDialog(title, message, icon);
    }

    public static MessageDialog createMessageDialog(String title, String message, MessageDialog.DialogType dialogType, MessageDialog.MessageIcon messageIcon, MessageDialog.MessageDefault messageDefault) {
        return new MessageDialog(title, message, dialogType, messageIcon, messageDefault);
    }

    // set defaultMessage to null for password field
    public static InputDialog createInputDialog(String title, String message, String defaultMessage) {
        return new InputDialog(title, message, defaultMessage);
    }

    public static FileChooserDialog createFileChooserDialog(String title, String defaultPath, FileFilter fileFilter, String description, boolean multipleSelect) {
        return new FileChooserDialog(title, defaultPath, fileFilter, description, multipleSelect);
    }

    public static FileSaveDialog createFileSaveDialog(String title, String defaultPath, FileFilter fileFilter, String description) {
        return new FileSaveDialog(title, defaultPath, fileFilter, description);
    }

    public static FolderSelectDialog createFolderSelectDialog(String title, String defaultPath) {
        return new FolderSelectDialog(title, defaultPath);
    }

    // defaultColor ignore alpha
    public static ColorChooserDialog createColorChooserDialog(String title, Color defaultColor) {
        return new ColorChooserDialog(title, defaultColor);
    }


    // For file dialog
    public class FileFilter {

        private List<String> extensions;

        public FileFilter() {
            this.extensions = new ArrayList<>();
        }

        public FileFilter(String extension) {
            this();
            this.extensions.add(extension);
        }

        public FileFilter add(String extension) {
            this.extensions.add(extension);
            return this;
        }

        public PointerBuffer create(MemoryStack stack) {
            PointerBuffer pattern = stack.mallocPointer(extensions.size());
            for (String extension : extensions) {
                pattern.put(stack.UTF8("*." + extension));
            }
            pattern.flip();
            return pattern;
        }

    }

}

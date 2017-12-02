package fr.proneus.engine.filedialog;

import fr.proneus.engine.graphic.Color;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class ColorChooserDialog {

    private Color color;

    protected ColorChooserDialog(String title, Color defaultColor) {
        try (MemoryStack stack = stackPush()) {
            ByteBuffer colorBuffer = stack.malloc(3);
            String hexColor = String.format("#%02x%02x%02x", defaultColor.r, defaultColor.g, defaultColor.b);
            String hex = tinyfd_colorChooser(title, hexColor, null, colorBuffer);
            if (hex != null) {
                int red = (colorBuffer.get(0) & 0xFF);
                int green = (colorBuffer.get(1) & 0xFF);
                int blue = (colorBuffer.get(2) & 0xFF);
                color = new Color(red, green, blue);
            }
        }
    }

    public Color getColor() {
        return color;
    }
}

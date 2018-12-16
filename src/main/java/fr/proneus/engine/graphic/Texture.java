package fr.proneus.engine.graphic;

import fr.proneus.engine.utils.ByteBufferUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30C.*;

public class Texture {

    private Image image;
    private int textureId;

    public Texture(Image image) {
        this.image = image;
        this.textureId = loadTexture(image.getBufferedImage());
    }

    public Texture(InputStream inputStream) {
        this(new Image(inputStream));
    }

    public Texture(File file) {
        this(new Image(file));
    }

    public Image getImage() {
        return image;
    }

    public int getTextureId() {
        return textureId;
    }

    public void delete() {
        glDeleteTextures(textureId);
    }

    private int loadTexture(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = ByteBufferUtils.convertImage(image);

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glBindTexture(GL_TEXTURE_2D, 0);

        return textureID;
    }
}

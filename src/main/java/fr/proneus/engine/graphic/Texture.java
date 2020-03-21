package fr.proneus.engine.graphic;

import fr.proneus.engine.utils.ByteBufferUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL30C.*;

public class Texture implements ITexture {

    private int imageWidth, imageHeight;
    private int textureId;

    private Image image;

    public Texture(ByteBuffer buffer, int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.textureId = loadTexture(buffer, imageWidth, imageHeight);
    }

    public Texture(Bitmap bitmap) {
        this(ByteBufferUtils.convertImage(bitmap.getPixels(), bitmap.getWidth(), bitmap.getHeight()),
                bitmap.getWidth(),
                bitmap.getHeight());
    }

    public Texture(Image image) {
        this(ByteBufferUtils.convertImage(image.getBufferedImage()),
                image.getBufferedImage().getWidth(),
                image.getBufferedImage().getHeight());
        this.image = image;
    }

    public Texture(InputStream inputStream) {
        this(new Image(inputStream));
    }

    public Texture(File file) {
        this(new Image(file));
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public boolean hasImage() {
        return image != null;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public int getTextureId() {
        return textureId;
    }

    private int loadTexture(ByteBuffer buffer, int imageWidth, int imageHeight) {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, imageWidth, imageHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glBindTexture(GL_TEXTURE_2D, 0);

        return textureID;
    }
}

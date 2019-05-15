package fr.proneus.engine.graphic;

import fr.proneus.engine.utils.IOUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.system.MemoryStack.stackPush;

public class FontTexture implements ITexture {

    private String path;
    private int size;
    private int bitmapSize;
    private int textureId;
    private STBTTBakedChar.Buffer fontBuffer;

    public FontTexture(String path, int size) {
        this.path = path;
        this.size = size;
        this.bitmapSize = 2048;
        this.fontBuffer = load(size);
    }

    public FontTexture(String path) {
        this(path, 32);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.fontBuffer = load(size);
    }

    public int getBitmapSize() {
        return bitmapSize;
    }

    @Override
    public int getTextureId() {
        return textureId;
    }

    public STBTTBakedChar.Buffer getFontBuffer() {
        return fontBuffer;
    }

    public float[] getTextSize(String text) {
        float[] result = {0, 0};
        if (text == null || text.equals(""))
            return result;
        float width = 0;
        float height = 0;

        try (MemoryStack stack = stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) + size);
                    xx.put(0, 0.0f);
                    continue;
                } //else if (c < 32 || 128 <= c)
                //continue;
                if (c == ' ') {
                    // Might be inexact, space is normally equal to 0
                    c = 'M';
                }
                stbtt_GetBakedQuad(fontBuffer, bitmapSize, bitmapSize, c - 32, xx, yy, q, true);
                float x1 = q.x1() / bitmapSize;
                width = x1;

                float y0 = q.y0() / bitmapSize;
                // Keep the max height
                height = Math.max(height, Math.abs(y0));
            }
        }
        // Aspect ratio
        width /= 16f / 9f;

        result[0] = width;
        result[1] = height;

        return result;
    }

    public float getWidth(String text) {
        return getTextSize(text)[0];
    }

    public float getHeight(String text) {
        return getTextSize(text)[1];
    }

    private STBTTBakedChar.Buffer load(int size) {
        textureId = glGenTextures();
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(65535);

        try {
            ByteBuffer ttf = IOUtil.ioResourceToByteBuffer("/" + path, 160 * 1024);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapSize * bitmapSize);
            stbtt_BakeFontBitmap(ttf, size, bitmap, bitmapSize, bitmapSize, 32, cdata);

            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RED, bitmapSize, bitmapSize, 0, GL_RED, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return cdata;
    }
}

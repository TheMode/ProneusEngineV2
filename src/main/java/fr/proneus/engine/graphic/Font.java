package fr.proneus.engine.graphic;

import fr.proneus.engine.graphic.shader.Shaders;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Font extends Renderable {

    private int vao, vbo, indicesVBO;

    // Char index/data
    private Map<Integer, float[]> quads;
    private String currentText;

    private FontTexture texture;

    private Matrix4f mvp;
    private Matrix4f projection;

    public Font(FontTexture texture) {
        this.texture = texture;
        this.quads = new HashMap<>();

        float[] data = new float[4 * 4];
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();

        byte[] indices = {
                0, 1, 2,
                2, 3, 0
        };
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        indicesVBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        this.shader = Shaders.getFontShader();

        // Default values
        setPosition(0, 0, 0);
        scale(1, 1);
        setAngle(0);
        positionOrigin = Origin.CENTER_LEFT;
        rotateOrigin = Origin.CENTER_LEFT;
        scaleOrigin = Origin.CENTER_LEFT;
        refreshModel();
        setColor(Color.WHITE);

        this.currentText = "";
    }

    @Override
    public void render() {
        if (currentText == null)
            throw new NullPointerException("Text is null Font#setText");

        if (shouldRefreshModel)
            refreshModel();

        shader.setMat4("mvp", mvp);
        shader.setVec4("color", color);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBO);

        drawText();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private void drawText() {

        for (int i = 0; i < currentText.length(); i++) {
            float[] data = quads.get(i);
            if (data == null)
                continue;

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, data);

            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
        }
    }

    public String getText() {
        return currentText;
    }

    public void setText(String text) {
        if (text == null)
            return;
        if (currentText != null && currentText.equals(text))
            return;

        quads.clear();
        this.currentText = text;

        float[] size = texture.getTextSize(text);
        this.width = size[0];//texture.getWidth(text);
        this.height = size[1];//texture.getHeight(text);

        int bitmapSize = texture.getBitmapSize();
        try (MemoryStack stack = stackPush()) {
            FloatBuffer xx = stack.floats(0.0f);
            FloatBuffer yy = stack.floats(0.0f);
            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    yy.put(0, yy.get(0) + texture.getSize());
                    xx.put(0, 0.0f);
                    continue;
                } //else if (c < 32 || 128 <= c)
                //continue;
                stbtt_GetBakedQuad(texture.getFontBuffer(), bitmapSize, bitmapSize, c - 32, xx, yy, q, true);
                float[] data = {
                        q.x0() / bitmapSize, q.y0() / bitmapSize, q.s0(), q.t0(),
                        q.x1() / bitmapSize, q.y0() / bitmapSize, q.s1(), q.t0(),
                        q.x1() / bitmapSize, q.y1() / bitmapSize, q.s1(), q.t1(),
                        q.x0() / bitmapSize, q.y1() / bitmapSize, q.s0(), q.t1()
                };

                quads.put(i, data);
            }
        }
        this.shouldRefreshModel = true;
    }

    public void refreshModel() {
        this.shouldRefreshModel = false;

        this.projection = new Matrix4f().ortho(0, RATIO, 1, 0, -1, 1);

        refreshModelMatrix();

        this.mvp = projection.mul(model);
    }

    public void delete() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(indicesVBO);
        glDeleteVertexArrays(vao);
    }
}

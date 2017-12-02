package fr.proneus.engine.graphic;

import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryStack;

import fr.proneus.engine.utils.IOUtil;

public class Font {

	private String path;
	private int size;
	private int texID;

	private STBTTBakedChar.Buffer cdata;

	public Font(String path, int size) {
		this.path = path;
		this.size = size;
		
	}

	protected void draw(String text, float x, float y, FontStyle style, Color color) {
		if (!isLoaded())
			cdata = load();
		
		switch (style) {
		case LEFT:
			x -= getWidth(text);
			break;
		case CENTERED:
			x -= getWidth(text) / 2;
			break;
		case RIGHT:
			break;
		}

		try (MemoryStack stack = stackPush()) {
			FloatBuffer xx = stack.floats(0.0f);
			FloatBuffer yy = stack.floats(0.0f);
			STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
			glPushMatrix();
			glBindTexture(GL_TEXTURE_2D, texID);

            float newX = x * 1920f;
            float newY = y * 1080f;

            glTranslatef(newX, newY, 0f);
            glColor4f((float) color.r / 255, (float) color.g / 255, (float) color.b / 255, color.a);
			glBegin(GL_QUADS);
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c == '\n') {
					yy.put(0, yy.get(0) + size);
					xx.put(0, 0.0f);
					continue;
				} else if (c < 32 || 128 <= c)
					continue;
				stbtt_GetBakedQuad(cdata, 512, 512, c - 32, xx, yy, q, true);
				glTexCoord2f(q.s0(), q.t0());
				glVertex2f(q.x0(), q.y0());

				glTexCoord2f(q.s1(), q.t0());
				glVertex2f(q.x1(), q.y0());

				glTexCoord2f(q.s1(), q.t1());
				glVertex2f(q.x1(), q.y1());

				glTexCoord2f(q.s0(), q.t1());
				glVertex2f(q.x0(), q.y1());
			}
			glEnd();

			glColor4f(1, 1, 1, 1);

			glPopMatrix();
		}
	}

	private STBTTBakedChar.Buffer load() {
		texID = glGenTextures();
		STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96);

		try {
			ByteBuffer ttf = IOUtil.ioResourceToByteBuffer(path, 160 * 1024);

			ByteBuffer bitmap = BufferUtils.createByteBuffer(512 * 512);
			stbtt_BakeFontBitmap(ttf, size, bitmap, 512, 512, 32, cdata);

			glBindTexture(GL_TEXTURE_2D, texID);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, 512, 512, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return cdata;
	}

	public float getWidth(String text) {
		if (text == null || text.equals(""))
			return 0;
		
		if (!isLoaded())
			cdata = load();
		
		float length;

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
				} else if (c < 32 || 128 <= c)
					continue;
				stbtt_GetBakedQuad(cdata, 512, 512, c - 32, xx, yy, q, true);
			}
			length = q.x1();
		}

        return length / 1920f;
    }
	
	public float getHeight(String text) {
		if (text == null || text.equals(""))
			return 0;
		
		if (!isLoaded())
			cdata = load();
		
		float length = 0;

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
				} else if (c < 32 || 128 <= c)
					continue;
				stbtt_GetBakedQuad(cdata, 512, 512, c - 32, xx, yy, q, true);
				if(-q.y0() > length){
					length = q.y0();
				}
			}
		}

		length = length > 0 ? length*2 : -length;

        return length / 1080f;
    }

	private boolean isLoaded() {
		return texID != 0;
	}

	public void setSizeBeforeFirstDraw(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public enum FontStyle {
        RIGHT, LEFT, CENTERED
    }
}
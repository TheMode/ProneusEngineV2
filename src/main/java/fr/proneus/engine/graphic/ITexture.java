package fr.proneus.engine.graphic;

import static org.lwjgl.opengl.GL30C.glDeleteTextures;

public interface ITexture {

    int getTextureId();

    default void delete() {
        glDeleteTextures(getTextureId());
    }

}

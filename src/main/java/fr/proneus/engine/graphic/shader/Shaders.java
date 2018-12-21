package fr.proneus.engine.graphic.shader;

import fr.proneus.engine.graphic.Shader;

public class Shaders {

    private static Shader defaultSpriteShader;
    private static Shader fontShader;

    static {
        defaultSpriteShader = new Shader("shader/vertex.vs", "shader/fragment.fs");
        fontShader = new Shader("shader/fontvertex.vs", "shader/fontfragment.fs");
    }

    public static Shader getDefaultSpriteShader() {
        return defaultSpriteShader;
    }

    public static Shader getFontShader() {
        return fontShader;
    }
}

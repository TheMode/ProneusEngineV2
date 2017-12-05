package fr.proneus.engine.light;

import fr.proneus.engine.Game;
import fr.proneus.engine.light.Light.LightType;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class LightManager {

    private Map<Integer, Light> lights;
    private int id;

    public LightManager() {
        this.lights = new HashMap<>();
    }

    public Light getLight(int id) {
        return lights.get(id);
    }

    public Map<Integer, Light> getLights() {
        return lights;
    }

    public int addLight(Light light) {
        int id = this.id++;
        lights.put(id, light);
        return id;
    }

    public void removeLight(int id) {
        lights.remove(id);
    }

    // In game class
    public void render(Game game) {
        for (Light light : lights.values()) {
            int shaderProgram = light.getShaderProgramID();
            glColorMask(false, false, false, false);
            glStencilFunc(GL_ALWAYS, 1, 1);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);

            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilFunc(GL_EQUAL, 0, 1);
            glColorMask(true, true, true, true);

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

            glUseProgram(shaderProgram);

            // Position
            float x = light.getX() * ((float) game.getWidth());
            float y = game.getHeight() - light.getY() * ((float) game.getHeight());

            // Shader uniforms
            glUniform1i(glGetUniformLocation(shaderProgram, "lightType"), light.getLightType().getID());
            glUniform2f(glGetUniformLocation(shaderProgram, "lightLocation"), x, y);
            glUniform2f(glGetUniformLocation(shaderProgram, "resolution"), Game.getDefaultWidth(), Game.getDefaultHeight());
            glUniform4f(glGetUniformLocation(shaderProgram, "lightColor"), (float) light.getColor().r / 255f,
                    (float) light.getColor().g / 255f, (float) light.getColor().b / 255f, light.getColor().a);

            if (light.getLightType().equals(LightType.AMBIENT)) {
                glUniform1f(glGetUniformLocation(shaderProgram, "ambientColor"), light.getAmbientColor());
                glUniform1f(glGetUniformLocation(shaderProgram, "ambientIntensity"), light.getAmbientIntensity());
                glUniform1f(glGetUniformLocation(shaderProgram, "ambientDecrease"), light.getAmbientDecrease());
                glUniform1f(glGetUniformLocation(shaderProgram, "ambientShowRange"), light.getAmbientShowRange());
            }

            // Draw
            glBegin(GL_QUADS);
            {
                glVertex2f(0, 0);
                glVertex2f(0, Game.getDefaultHeight());
                glVertex2f(Game.getDefaultWidth(), Game.getDefaultHeight());
                glVertex2f(Game.getDefaultWidth(), 0);
            }
            glEnd();

            glDisable(GL_BLEND);
            glUseProgram(0);
            glClear(GL_STENCIL_BUFFER_BIT);
        }
    }

}

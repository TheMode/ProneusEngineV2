package fr.proneus.engine.light;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_KEEP;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glStencilFunc;
import static org.lwjgl.opengl.GL11.glStencilOp;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.HashMap;
import java.util.Map;

import fr.proneus.engine.Game;
import fr.proneus.engine.light.Light.LightType;

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

			float x = light.getX() * ((float) game.getWidth() / (float) game.getVirtualWidth());
			float y = game.getHeight() - light.getY() * ((float) game.getHeight() / (float) game.getVirtualHeight());

			glUniform1i(glGetUniformLocation(shaderProgram, "lightType"), light.getLightType().getID());
			glUniform2f(glGetUniformLocation(shaderProgram, "lightLocation"), x, y);
			glUniform2f(glGetUniformLocation(shaderProgram, "resolution"), game.getWidth(), game.getHeight());
			glUniform4f(glGetUniformLocation(shaderProgram, "lightColor"), (float) light.getColor().r / 255f,
					(float) light.getColor().g / 255f, (float) light.getColor().b / 255f, light.getColor().a);

			if (light.getLightType().equals(LightType.AMBIENT)) {
				glUniform1f(glGetUniformLocation(shaderProgram, "ambientColor"), light.getAmbientColor());
				glUniform1f(glGetUniformLocation(shaderProgram, "ambientIntensity"), light.getAmbientIntensity());
				glUniform1f(glGetUniformLocation(shaderProgram, "ambientDecrease"), light.getAmbientDecrease());
				glUniform1f(glGetUniformLocation(shaderProgram, "ambientShowRange"), light.getAmbientShowRange());
			}

			glBegin(GL_QUADS);
			{
				glVertex2f(0, 0);
				glVertex2f(0, game.getVirtualHeight());
				glVertex2f(game.getVirtualWidth(), game.getVirtualHeight());
				glVertex2f(game.getVirtualWidth(), 0);
			}
			glEnd();

			glDisable(GL_BLEND);
			glUseProgram(0);
			glClear(GL_STENCIL_BUFFER_BIT);
		}
	}

}

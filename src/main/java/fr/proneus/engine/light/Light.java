package fr.proneus.engine.light;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.*;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.Color;
import fr.proneus.engine.utils.FileUtils;

public class Light {

	private int fragmentShader;
	private int shaderProgram;

	private boolean enabled;

	private Color color;
	private float x, y ;
	private LightType lightType;
	
	// Ambient
	private float ambientColor = 1f;
	private float ambientIntensity = 1f;
	private float ambientDecrease = 1f;
	private float ambientShowRange = 0f;

	public Light(float x, float y, Color color, LightType lightType) {
		this.x = x;
		this.y = y;
		this.color = Color.WHITE;
		this.lightType = lightType;
		load();
	}
	
	public Light(float x, float y){
		this(x, y, Color.WHITE, LightType.AMBIENT);
	}
	
	private void load(){
		shaderProgram = glCreateProgram();
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		StringBuilder fragmentShaderSource = new StringBuilder();

		try {
			String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getInternalFile("/shader/light.frag")));
            while ((line = reader.readLine()) != null) {
				fragmentShaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Fragment shader not compiled!");
		}

		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
	}

	public int getFragmentShaderID() {
		return fragmentShader;
	}

	public int getShaderProgramID() {
		return shaderProgram;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Color getColor() {
		return color;
	}

    public void setColor(Color color) {
        this.color = color;
    }
	
	public LightType getLightType() {
		return lightType;
	}

    public void setLightType(LightType lightType) {
        this.lightType = lightType;
    }

    public float getAmbientColor() {
		return ambientColor;
	}
	
	public void setAmbientColor(float ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public float getAmbientIntensity() {
		return ambientIntensity;
	}
	
	public void setAmbientIntensity(float ambientIntensity) {
		this.ambientIntensity = ambientIntensity;
	}
	
	public float getAmbientDecrease() {
		return ambientDecrease;
	}
	
	public void setAmbientDecrease(float ambientDecrease) {
		this.ambientDecrease = ambientDecrease;
	}
	
	public float getAmbientShowRange() {
		return ambientShowRange;
	}
	
	public void setAmbientShowRange(float ambientShowRange) {
		this.ambientShowRange = ambientShowRange;
	}

	public enum LightType {
		AMBIENT(0), SOLEIL(1);

		private int id;

		private LightType(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}
	}
}

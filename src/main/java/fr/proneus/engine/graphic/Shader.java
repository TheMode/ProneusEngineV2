package fr.proneus.engine.graphic;

import fr.themode.utils.file.FileUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30C.*;

public class Shader {

    private int ID;

    public Shader(String vertexPath, String fragmentPath) {
        String vertexString = FileUtils.getInternalFileString(vertexPath);
        String fragmentString = FileUtils.getInternalFileString(fragmentPath);
        int vertex, fragment;

        vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexString);
        glCompileShader(vertex);
        checkCompileErrors(vertex, "VERTEX");

        fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment, fragmentString);
        glCompileShader(fragment);
        checkCompileErrors(fragment, "FRAGMENT");

        this.ID = glCreateProgram();
        glAttachShader(ID, vertex);
        glAttachShader(ID, fragment);
        glLinkProgram(ID);
        checkCompileErrors(ID, "PROGRAM");

        glDeleteShader(vertex);
        glDeleteShader(fragment);
    }

    public void use() {
        glUseProgram(ID);
    }

    public void setBool(String name, boolean value) {
        glUniform1i(glGetUniformLocation(ID, name), value ? 1 : 0);
    }

    // ------------------------------------------------------------------------
    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(ID, name), value);
    }

    // ------------------------------------------------------------------------
    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(ID, name), value);
    }

    // ------------------------------------------------------------------------
    public void setVec2(String name, Vector2f vector) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(2);
        vector.get(fb);
        glUniform2fv(glGetUniformLocation(ID, name), fb);
    }

    public void setVec2(String name, float x, float y) {
        glUniform2f(glGetUniformLocation(ID, name), x, y);
    }

    // ------------------------------------------------------------------------
    public void setVec3(String name, Vector3f vector) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(3);
        vector.get(fb);
        glUniform3fv(glGetUniformLocation(ID, name), fb);
    }

    public void setVec3(String name, float x, float y, float z) {
        glUniform3f(glGetUniformLocation(ID, name), x, y, z);
    }

    // ------------------------------------------------------------------------
    public void setVec4(String name, Vector4f vector) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4);
        vector.get(fb);
        glUniform4fv(glGetUniformLocation(ID, name), fb);
    }

    public void setVec4(String name, float x, float y, float z, float w) {
        glUniform4f(glGetUniformLocation(ID, name), x, y, z, w);
    }

    // ------------------------------------------------------------------------
    public void setMat4(String name, Matrix4f matrix) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4 * 4);
        matrix.get(fb);
        glUniformMatrix4fv(glGetUniformLocation(ID, name), false, fb);
    }

    private void checkCompileErrors(int shader, String type) {
        IntBuffer success = BufferUtils.createIntBuffer(1);
        char[] infoLog;
        if (type != "PROGRAM") {
            glGetShaderiv(shader, GL_COMPILE_STATUS, success);
            int result = success.get();
            if (result == 0) {
                System.out.println("ERROR::SHADER_COMPILATION_ERROR of type: \n" + glGetShaderInfoLog(shader));
            }
        } else {
            glGetProgramiv(shader, GL_LINK_STATUS, success);
            int result = success.get();
            if (result == 0) {
                System.out.println("ERROR::SHADER_COMPILATION_ERROR of type: \n" + glGetProgramInfoLog(shader));
            }
        }
    }
}

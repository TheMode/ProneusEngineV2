package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.themode.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30C.*;

public class Sprite {

    private final float RATIO = Game.getAspectRatio();

    private float[] vertices;
    private int vao, verticesVBO, texturesVBO, indicesVBO;
    private float width, height;

    private Matrix4f mvp;
    private Matrix4f projection;
    private Matrix4f model;
    private float[] finalVertices;

    private Vector3f position;
    private float scaleX, scaleY;
    private float angle;

    private Origin positionOrigin, rotateOrigin, scaleOrigin;

    private boolean shouldRefreshModel;

    private Vector4f color;

    private Texture texture;

    // Test
    private BoundingBox boundingBox;

    // Animation
    private Map<String, Animation> animations;
    private Animation currentAnimation;
    private String currentAnimationName;
    private int currentAnimationFrame;
    private int currentAnimationMaxFrame;
    private int currentAnimationDelay;

    private long lastAnimationUpdate;

    public Sprite(Texture texture) {
        float vertices[] = new float[2 * 4];
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();

        float textures[] = {
                1f, 1f,
                0f, 1f,
                0f, 0f,
                1f, 0f
        };

        FloatBuffer texturesBuffer = BufferUtils.createFloatBuffer(textures.length);
        texturesBuffer.put(textures);
        texturesBuffer.flip();

        byte indices[] = {
                0, 1, 2,
                2, 3, 0
        };
        ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        verticesVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, verticesVBO);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        texturesVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, texturesVBO);
        glBufferData(GL_ARRAY_BUFFER, texturesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        indicesVBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // Default values
        setTexture(texture);
        applyTextureSize();
        setPosition(0, 0, 0);
        scale(1, 1);
        positionOrigin = Origin.CENTER;
        rotateOrigin = Origin.CENTER;
        scaleOrigin = Origin.CENTER;
        setColor(Color.WHITE);

        // Animation
        this.animations = new HashMap<>();

        // Bounding Box
        this.boundingBox = new BoundingBox();

        // Refresh model
        refreshModel();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setVertices(float[] vertices) {
        glBindBuffer(GL_ARRAY_BUFFER, verticesVBO);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    public void setSize(float width, float height) {

        width *= RATIO;

        this.width = width;
        this.height = height;

        float _width = width / 2;
        float _height = height / 2;

        float data[] = {
                -_width, _height, // Top-left
                _width, _height, // Top-right
                _width, -_height, // Bottom-right
                -_width, -_height  // Bottom-left
        };

        this.vertices = data;
        setVertices(data);
    }

    public void applyTextureSize() {
        if (texture == null)
            return;

        Image image = texture.getImage();
        float width = image.getImageWidth();
        float height = image.getImageHeight();

        setSize(width, height);
    }

    public void setTextureCoordinate(float x, float y, float width, float height) {
        x = MathUtils.minMax(x, 0, 1);
        y = MathUtils.minMax(y, 0, 1);
        width = MathUtils.minMax(width, 0, 1);
        height = MathUtils.minMax(height, 0, 1);

        glBindBuffer(GL_ARRAY_BUFFER, texturesVBO);

        float data[] = {
                x, height,
                width, height,
                width, y,
                x, y
        };

        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
    }

    public void draw(Shader shader) {
        if (shouldRefreshModel) {
            refreshModel();
        }

        shader.setMat4("mvp", mvp);
        shader.setVec4("color", color);

        if (texture != null)
            glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBO);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        if (texture != null)
            glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void setPosition(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
        this.shouldRefreshModel = true;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3f(x, y, position.z);
        this.shouldRefreshModel = true;
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        this.position.x = x;
        this.shouldRefreshModel = true;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        this.position.y = y;
        this.shouldRefreshModel = true;
    }

    public float getZ() {
        return position.z;
    }

    public void setZ(float z) {
        this.position.z = z;
        this.shouldRefreshModel = true;
    }

    public void scale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.shouldRefreshModel = true;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        this.shouldRefreshModel = true;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        this.shouldRefreshModel = true;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.shouldRefreshModel = true;
    }

    public void setPositionOrigin(Origin positionOrigin) {
        this.positionOrigin = positionOrigin;
        this.shouldRefreshModel = true;
    }

    public void setRotateOrigin(Origin rotateOrigin) {
        this.rotateOrigin = rotateOrigin;
        this.shouldRefreshModel = true;
    }

    public void setScaleOrigin(Origin scaleOrigin) {
        this.scaleOrigin = scaleOrigin;
        this.shouldRefreshModel = true;
    }

    public void setColor(Color color) {
        this.color = new Vector4f(color.r, color.g, color.b, color.a);
    }

    public void refreshModel() {
        this.shouldRefreshModel = false;

        this.projection = new Matrix4f().ortho(0, RATIO, 1, 0, -1, 1);

        this.model = new Matrix4f();

        float translateOffsetX = width * positionOrigin.getWidthModifier();
        float translateOffsetY = height * positionOrigin.getHeightModifier();
        this.model.translate((position.x + translateOffsetX) * RATIO, position.y + translateOffsetY, position.z);

        this.model.scale(scaleX, scaleY, 0);

        float rotateWidth = rotateOrigin.getWidthModifier() * width;
        float rotateHeight = rotateOrigin.getHeightModifier() * height;
        this.model.translate(rotateWidth, rotateHeight, 0);
        this.model.rotate((float) Math.toRadians(angle), new Vector3f(0, 0, 1));
        this.model.translate(-rotateWidth, -rotateHeight, 0);

        this.mvp = projection.mul(model);

        // Vertices calculation
        Vector4f v1 = new Vector4f(vertices[0], vertices[1], 0, 1).mul(model);
        Vector4f v2 = new Vector4f(vertices[2], vertices[3], 0, 1).mul(model);
        Vector4f v3 = new Vector4f(vertices[4], vertices[5], 0, 1).mul(model);
        Vector4f v4 = new Vector4f(vertices[6], vertices[7], 0, 1).mul(model);
        v1.x /= RATIO;
        v2.x /= RATIO;
        v3.x /= RATIO;
        v4.x /= RATIO;

        float[] finalVertices = {
                v1.x, v1.y,
                v2.x, v2.y,
                v3.x, v3.y,
                v4.x, v4.y
        };

        this.finalVertices = finalVertices;

        float[][] points = new float[4][2];
        points[0] = new float[]{v1.x, v1.y};
        points[1] = new float[]{v2.x, v2.y};
        points[2] = new float[]{v3.x, v3.y};
        points[3] = new float[]{v4.x, v4.y};

        // Bounding Box
        this.boundingBox.points = points;
    }

    public boolean interacts(float x, float y) {
        return boundingBox.interacts(x, y);
    }

    public boolean interacts(Sprite sprite) {
        if (sprite.finalVertices == null || finalVertices == null)
            throw new NullPointerException("Final vertives hasn't been calculated Sprite#refreshModel");

        float[] currentVertices = finalVertices;
        int currentLength = currentVertices.length;

        float[] targetVertices = sprite.finalVertices;
        int targetLength = targetVertices.length;

        int max = Math.max(currentLength, targetLength);
        for (int i = 0; i < max; i += 2) {
            float x1 = currentVertices[i];
            float y1 = currentVertices[i + 1];

            float x2 = targetVertices[i];
            float y2 = targetVertices[i + 1];

            if (sprite.interacts(x1, y1) || interacts(x2, y2))
                return true;

        }

        return false;
    }

    public void delete() {
        glDeleteBuffers(verticesVBO);
        glDeleteBuffers(texturesVBO);
        glDeleteBuffers(indicesVBO);
        glDeleteVertexArrays(vao);
    }

    // Animation

    public void updateAnimation() {
        if (currentAnimation == null)
            return;

        if (System.currentTimeMillis() - lastAnimationUpdate > currentAnimationDelay) {
            refreshAnimation();
        }
    }

    public void addAnimation(String name, Animation animation) {
        this.animations.put(name, animation);
    }

    public void setAnimation(String name, int delay) {
        this.currentAnimation = animations.get(name);
        if (this.currentAnimation == null)
            return;
        this.currentAnimationName = name;
        this.currentAnimationFrame = 0;
        this.currentAnimationMaxFrame = currentAnimation.frames.size();
        this.currentAnimationDelay = delay;
        refreshAnimation();
    }

    public void setAnimationFrame(int index) {
        index = MathUtils.minMax(index, 0, currentAnimationMaxFrame);
        this.currentAnimationFrame = index;
        this.lastAnimationUpdate = System.currentTimeMillis();
    }

    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

    public void disableAnimation() {
        this.currentAnimation = null;
        this.currentAnimationName = null;
        this.currentAnimationFrame = 0;
        this.currentAnimationMaxFrame = 0;
        this.currentAnimationDelay = 0;
        this.lastAnimationUpdate = 0;
    }

    private void refreshAnimation() {
        this.lastAnimationUpdate = System.currentTimeMillis();
        AnimationFrame frame = currentAnimation.frames.get(currentAnimationFrame);

        Texture frameTexture = frame.texture;
        if (texture != frameTexture)
            setTexture(frameTexture);

        Image image = texture.getImage();
        float width = image.getImageWidth() * frame.width;
        float height = image.getImageHeight() * frame.height;
        setSize(width, height);

        float textX = frame.x;
        float textY = frame.y;
        float textWidth = frame.width * (currentAnimationFrame + 1);
        float textHeight = frame.height * (currentAnimationFrame + 1);

        setTextureCoordinate(textX, textY, textWidth, textHeight);
        // setTextureCoordinate(frame.x, frame.y, frame.width, frame.height);
        // System.out.println(frame.x + " : " + frame.y + " : " + frame.width + " : " + frame.height);
        //onAnimationFrame(getAnimationName(), currentAnimationFrame);
        if (currentAnimation.frames.get(currentAnimationFrame + 1) == null) {
            // System.out.println("END ANIM");
            // onAnimationEnd(getAnimationName());
            currentAnimationFrame = 0;
        } else {
            currentAnimationFrame++;
        }
    }

    class BoundingBox {

        float[][] points;

        private boolean interacts(float x, float y) {
            int sides = 4;
            int j = sides - 1;
            boolean pointStatus = false;
            for (int i = 0; i < sides; i++) {
                if (points[i][1] < y && points[j][1] >= y || points[j][1] < y && points[i][1] >= y) {
                    if (points[i][0] + (y - points[i][1]) / (points[j][1] - points[i][1]) * (points[j][0] - points[i][0]) < x) {
                        pointStatus = !pointStatus;
                    }
                }
                j = i;
            }
            return pointStatus;
        }


    }
}

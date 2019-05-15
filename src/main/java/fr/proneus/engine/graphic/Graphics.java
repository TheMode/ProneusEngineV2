package fr.proneus.engine.graphic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graphics {

    private static final Map<Shader, List<Renderable>> renderables = new HashMap<>();
    // TODO camera projection matrix
    private Camera camera;

    public Graphics(Camera camera) {
        this.camera = camera;
    }

    protected void addRenderable(Renderable renderable) {
        Shader shader = renderable.getShader();
        List<Renderable> renderables = Graphics.renderables.getOrDefault(shader, new LinkedList<>());
        renderables.add(renderable);
        Graphics.renderables.put(shader, renderables);
    }

    public void renderFrame() {
        for (Map.Entry<Shader, List<Renderable>> entry : renderables.entrySet()) {
            Shader shader = entry.getKey();
            List<Renderable> renderables = entry.getValue();

            shader.use();
            for (Renderable renderable : renderables) {
                renderable.render();
            }
        }
        renderables.clear();
    }

}

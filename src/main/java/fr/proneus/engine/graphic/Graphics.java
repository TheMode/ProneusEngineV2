package fr.proneus.engine.graphic;

import fr.proneus.engine.Game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graphics {

    private static final Map<Shader, List<Renderable>> renderables = new HashMap<>();
    private Color backgroundColor = Color.BLACK;

    private Game game;

    public Graphics(Game game) {
        this.game = game;
    }

    protected void addRenderable(Renderable renderable) {
        Shader shader = renderable.getShader();
        List<Renderable> renderables = Graphics.renderables.getOrDefault(shader, new LinkedList<>());
        renderables.add(renderable);
        Graphics.renderables.put(shader, renderables);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    // Unsafe, only used by the Game class
    public void renderFrame() {
        for (Map.Entry<Shader, List<Renderable>> entry : renderables.entrySet()) {
            Shader shader = entry.getKey();
            List<Renderable> renderables = entry.getValue();

            shader.use();
            shader.setVec2("camera", game.getCamera().getPosition());
            for (Renderable renderable : renderables) {
                renderable.render();
            }
        }
        renderables.clear();
    }

}

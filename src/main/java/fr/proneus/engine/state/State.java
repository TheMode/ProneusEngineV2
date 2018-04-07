package fr.proneus.engine.state;

import fr.proneus.engine.Game;
import fr.proneus.engine.discord.DiscordRPCJoinRequest;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Renderable;
import fr.proneus.engine.gui.Component;
import fr.proneus.engine.light.LightManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class State {

    private List<Renderable> renderables = new ArrayList<>();
    private List<Component> components = new ArrayList<>();
    private LightManager lightManager = new LightManager();

    public abstract void create(Game game);

    public abstract void update(Game game);

    public abstract void render(Game game, Graphics graphic);

    public abstract void exit(Game game);

    public void onKeyRepeat(Game game, int key, int scancode) {
    }

    public void onKeyDown(Game game, int key, int scancode) {
    }

    public void onKeyUp(Game game, int key, int scancode) {
    }

    public void onCharCallback(Game game, char character){
    }

    public void onMouseMove(Game game, float x, float y) {
    }

    public void onMouseScroll(Game game, float power) {
    }

    public void onControllerConnect(Game game, int joy) {
    }

    public void onControllerDisconnect(Game game, int joy) {
    }

    // Discord
    public void onDiscordRPCReady(Game game) {
    }

    public void onDiscordRPCDisconnected(Game game, String message) {
    }

    public void onDiscordRPCErrored(Game game, String message) {
    }

    public void onDiscordRPCJoinGame(Game game, String secret) {
    }

    public void onDiscordRPCSpectateGame(Game game, String secret) {
    }

    public void onDiscordRPCJoinRequest(Game game, DiscordRPCJoinRequest joinRequest) {
    }

    // Sprites
    public void renderablesUpdate(Game game) {
        for (Renderable renderable : renderables) {
            renderable.update(game);
        }
    }

    public List<Renderable> getLoadedRenderables() {
        return renderables;
    }

    public <T extends Renderable> T createRenderable(Renderable renderable) {
        renderables.add(renderable);
        return (T) renderable;
    }

    public void removeRenderable(Renderable renderable) {
        Iterator<Renderable> iter = renderables.iterator();

        while (iter.hasNext()) {
            Renderable r = iter.next();

            if (r.equals(renderable)) {
                iter.remove();
                break;
            }
        }
    }

    // Components
    public void componentsUpdate(Game game) {
        for (Component comp : components) {
            if (comp.isVisible())
                comp.update(game);
        }
    }

    public void componentsRender(Game game, Graphics graphic) {
        for (Component comp : components) {
            if (comp.isVisible())
                comp.render(game, graphic);
        }
    }

    public List<Component> getComponents() {
        return components;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        Iterator<Component> iter = components.iterator();

        while (iter.hasNext()) {
            Component c = iter.next();

            if (c.equals(component)) {
                iter.remove();
                break;
            }
        }
    }

    public LightManager getLightManager() {
        return lightManager;
    }

}

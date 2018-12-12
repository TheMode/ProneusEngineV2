package fr.proneus.engine.motable;

import com.esotericsoftware.kryonet.Client;
import fr.proneus.engine.State;
import fr.proneus.engine.graphic.*;
import fr.proneus.engine.motable.listener.GameObjectListener;
import fr.proneus.engine.motable.manager.GameObjectManager;
import fr.proneus.engine.motable.manager.ScriptManager;
import fr.proneus.engine.motable.manager.TextureManager;
import fr.themode.motable.network.ScriptEvent;
import fr.themode.motable.network.packet.connection.ClientAnswerReady;
import fr.themode.utils.file.FileUtils;

import java.util.List;
import java.util.Map;

public class GameState extends State {

    private Client client;
    private ServerClient serverClient;

    private GameObjectManager gameObjectManager;
    private ScriptManager scriptManager;
    private TextureManager textureManager;

    private Shader objectShader;
    private Shader fontShader;
    private Texture texture;
    private Sprite object;
    private float angle;

    private FontTexture fontTexture;
    private Font fontObject;

    public GameState(Client client, ServerClient serverClient, List<Script> scripts, Map<String, String> textures) {
        this.client = client;
        this.serverClient = serverClient;
        this.textureManager = new TextureManager(textures);
        this.gameObjectManager = new GameObjectManager(textureManager);
        this.scriptManager = new ScriptManager(scripts);
    }

    @Override
    public void create() {
        this.scriptManager.setupBindings(this);

        textureManager.load();
        client.addListener(new GameObjectListener(gameObjectManager, textureManager));

        ClientAnswerReady readyPacket = new ClientAnswerReady();
        client.sendTCP(readyPacket);
        //Random r = new Random();
        // expose 'next gaussian' as script global function
        //e.put("gaussian", (Supplier<Double>) r::nextGaussian);


        this.objectShader = new Shader("shader/vertex.vs", "shader/fragment.fs");

        this.fontShader = new Shader("shader/fontvertex.vs", "shader/fontfragment.fs");

        texture = new Texture(new Image(FileUtils.getInternalFile("ship.png")));

        this.object = new Sprite(texture);
        this.object.applyTextureSize();
        this.object.setPosition(0.5f, 0.75f, 0);
        this.object.scale(5, 5);
        this.object.setRotateOrigin(Origin.TOP_LEFT);
        this.object.refreshModel();
        //this.object.setColor(Color.RED);
        //this.object.setTextureCoordinate(0, 0, 0.5f, 0.5f);

        this.fontTexture = new FontTexture("fonts/pixelfont.ttf", 128);
        this.fontObject = new Font(fontTexture);
        this.fontObject.setPosition(0.5f, 0.5f, 0);
        this.fontObject.scale(1, 1);
        this.fontObject.refreshModel();
        this.fontObject.setColor(Color.WHITE);

        this.fontObject.setText("I am a text !");
    }

    @Override
    public void update() {
        //System.out.println(getGame().getFps());
        gameObjectManager.updateObjects();
        scriptManager.callEvent(ScriptEvent.ON_TICK);
    }

    @Override
    public void render() {
        objectShader.use();
        gameObjectManager.drawObjects(objectShader);

        fontShader.use();
        fontObject.draw(fontShader);
    }

    @Override
    public void exit() {
        texture.delete();
        object.delete();
    }

    public Client getClient() {
        return client;
    }

    public ServerClient getServerClient() {
        return serverClient;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

}

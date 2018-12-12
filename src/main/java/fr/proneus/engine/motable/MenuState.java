package fr.proneus.engine.motable;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import fr.proneus.engine.State;
import fr.proneus.engine.motable.listener.FileListener;
import fr.proneus.engine.motable.listener.PingListener;
import fr.themode.motable.network.Network;
import fr.themode.motable.network.packet.connection.ClientConnectPacket;
import fr.themode.utils.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuState extends State {

    private Client client;
    private Listener fileListener;

    private ServerClient serverClient;

    private boolean ready;

    private List<Script> scripts;

    private Map<String, String> textures;

    @Override
    public void create() {

        this.scripts = new ArrayList<>();

        this.textures = new HashMap<>();

        this.client = new Client();
        this.client.start();
        Network.register(client);

        PingListener pingListener = new PingListener(client);

        this.fileListener = new FileListener(this);
        this.client.addListener(fileListener);
        this.client.addListener(pingListener);

        this.serverClient = new ServerClient("TheMode" + RandomUtils.getRandomInteger(0, 50000));

        joinServer("localhost", 25565, 25565);
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    @Override
    public void exit() {

    }

    private void joinServer(String host, int tcp, int udp) {
        try {
            client.connect(10000, host, tcp, udp);

            ClientConnectPacket connectPacket = new ClientConnectPacket();
            connectPacket.username = serverClient.getUsername();
            client.sendTCP(connectPacket);

            // Wait for files to be received or success confirmation
            while (!ready) {
                // System.out.println("Wait for files...");
                Thread.sleep(50);
            }

            client.removeListener(fileListener);
            getGame().setState(new GameState(client, serverClient, scripts, textures));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ready() {
        this.ready = true;
    }

    public void addScrip(Script script) {
        this.scripts.add(script);
    }

    public void addTexture(String fileId, String path) {
        this.textures.put(fileId, path);
    }

}

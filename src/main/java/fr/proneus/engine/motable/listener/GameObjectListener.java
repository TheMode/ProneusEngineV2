package fr.proneus.engine.motable.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import fr.proneus.engine.motable.manager.GameObjectManager;
import fr.proneus.engine.motable.manager.TextureManager;
import fr.themode.motable.network.packet.gameobject.GameObjectCreatePacket;
import fr.themode.motable.network.packet.gameobject.GameObjectDeletePacket;
import fr.themode.motable.network.packet.gameobject.GameObjectUpdatePacket;

public class GameObjectListener extends Listener {

    private GameObjectManager gameObjectManager;
    private TextureManager textureManager;

    public GameObjectListener(GameObjectManager gameObjectManager, TextureManager textureManager) {
        this.gameObjectManager = gameObjectManager;
        this.textureManager = textureManager;
    }

    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof GameObjectCreatePacket) {
            GameObjectCreatePacket packet = (GameObjectCreatePacket) object;
            this.gameObjectManager.addWaitingGameObject(packet);

        } else if (object instanceof GameObjectDeletePacket) {
            GameObjectDeletePacket packet = (GameObjectDeletePacket) object;
            this.gameObjectManager.addGameObjectToDelete(packet);

        } else if (object instanceof GameObjectUpdatePacket) {
            GameObjectUpdatePacket packet = (GameObjectUpdatePacket) object;
            this.gameObjectManager.updateGameObject(packet);

        }
    }

}

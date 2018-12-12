package fr.proneus.engine.motable.manager;

import fr.proneus.engine.graphic.Origin;
import fr.proneus.engine.graphic.Shader;
import fr.proneus.engine.graphic.Texture;
import fr.proneus.engine.motable.object.GameObject;
import fr.themode.motable.network.UpdateType;
import fr.themode.motable.network.packet.gameobject.GameObjectCreatePacket;
import fr.themode.motable.network.packet.gameobject.GameObjectDeletePacket;
import fr.themode.motable.network.packet.gameobject.GameObjectUpdatePacket;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameObjectManager {

    private TextureManager textureManager;

    private List<GameObject> objects;
    private Map<UUID, GameObject> objectsByUniqueId;
    private Map<String, GameObject> objectsByIdentifier;

    private List<GameObjectCreatePacket> waitingObjects;
    private List<UUID> objectsToDelete;
    private Map<UUID, CopyOnWriteArrayList<GameObjectUpdatePacket>> storedObjectsUpdate;

    public GameObjectManager(TextureManager textureManager) {
        this.textureManager = textureManager;

        this.objects = new ArrayList<>();
        this.objectsByUniqueId = new HashMap<>();
        this.objectsByIdentifier = new HashMap<>();

        this.waitingObjects = new ArrayList<>();
        this.objectsToDelete = new ArrayList<>();
        this.storedObjectsUpdate = new HashMap<>();
    }

    public void drawObjects(Shader shader) {
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            object.draw(shader);
        }
    }

    public void updateObjects() {
        deleteObjects();
        createWaitingObjects();
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            packetUpdates(object);
            // TODO script update
            // object.update();
        }
    }

    private void packetUpdates(GameObject object) {
        UUID uniqueId = object.getUniqueId();
        if (!this.storedObjectsUpdate.containsKey(uniqueId))
            return;
        List<GameObjectUpdatePacket> storedUpdates = this.storedObjectsUpdate.get(uniqueId);
        storedUpdates.forEach(storedPacket -> updateGameObject(object, storedPacket.updateType, storedPacket.values));
        this.storedObjectsUpdate.remove(uniqueId);
    }

    public List<GameObject> getGameObjects() {
        return objects;
    }

    public GameObject getGameObject(UUID uniqueId) {
        return objectsByUniqueId.get(uniqueId);
    }

    public GameObject getObjectByIdentifier(String identifier) {
        return objectsByIdentifier.get(identifier);
    }

    public void addWaitingGameObject(GameObjectCreatePacket packet) {
        this.waitingObjects.add(packet);
    }

    public void addGameObjectToDelete(GameObjectDeletePacket packet) {
        UUID uniqueId = UUID.fromString(packet.uniqueId);
        this.objectsToDelete.add(uniqueId);
    }

    public void updateGameObject(GameObjectUpdatePacket packet) {
        UUID uniqueId = UUID.fromString(packet.uniqueId);

        // Store packet in order to use them after object being successfully created
        CopyOnWriteArrayList<GameObjectUpdatePacket> storedUpdates = this.storedObjectsUpdate.computeIfAbsent(uniqueId, (list) -> new CopyOnWriteArrayList());
        storedUpdates.add(packet);
        this.storedObjectsUpdate.put(uniqueId, storedUpdates);
    }

    public void addGameObject(GameObject object) {
        if (objects.contains(object))
            return;
        this.objects.add(object);
        this.objectsByUniqueId.put(object.getUniqueId(), object);
        if (object.getIdentifier() != null)
            this.objectsByIdentifier.put(object.getIdentifier(), object);
    }

    public void removeGameObject(GameObject object) {
        this.objects.remove(object);
        this.objectsByUniqueId.remove(object.getUniqueId());

        String identifier = object.getIdentifier();
        if (identifier != null)
            objectsByIdentifier.remove(identifier);
        this.storedObjectsUpdate.remove(object.getUniqueId());
        object.delete();
    }

    public void removeGameObject(UUID uniqueId) {
        GameObject gameObject = objectsByUniqueId.get(uniqueId);
        if (gameObject != null) {
            objects.remove(gameObject);
            objectsByUniqueId.remove(uniqueId);

            String identifier = gameObject.getIdentifier();
            if (identifier != null)
                objectsByIdentifier.remove(identifier);
            this.storedObjectsUpdate.remove(uniqueId);
            gameObject.delete();
        }
    }

    private void createWaitingObjects() {

        for (int i = 0; i < waitingObjects.size(); i++) {
            GameObjectCreatePacket packet = waitingObjects.get(i);

            UUID uniqueId = UUID.fromString(packet.uniqueId);
            String fileId = packet.fileId;
            String identifier = packet.identifier;

            float x = packet.x;
            float y = packet.y;
            float z = packet.z;

            float angle = packet.angle;

            float scaleX = packet.scaleX;
            float scaleY = packet.scaleY;

            Origin positionOrigin = packet.positionOrigin;
            Origin rotateOrigin = packet.rotateOrigin;
            Origin scaleOrigin = packet.scaleOrigin;

            // getTextureByFileId
            System.out.println("[GameObjectManager] Object creation: " + fileId);
            Texture texture = textureManager.getTextureByFileId(fileId);

            GameObject gameObject = new GameObject(texture, uniqueId);
            gameObject.setIdentifier(identifier);
            gameObject.applyTextureSize();
            gameObject.setPosition(x, y, z);
            gameObject.setAngle(angle);
            gameObject.scale(scaleX, scaleY);
            gameObject.setPositionOrigin(positionOrigin);
            gameObject.setRotateOrigin(rotateOrigin);
            gameObject.setScaleOrigin(scaleOrigin);
            gameObject.refreshModel();

            addGameObject(gameObject);
            waitingObjects.remove(packet);
        }

    }

    private void deleteObjects() {
        for (int i = 0; i < objectsToDelete.size(); i++) {
            UUID uniqueId = objectsToDelete.get(i);
            removeGameObject(uniqueId);
        }
    }

    private void updateGameObject(GameObject object, UpdateType updateType, Object[] values) {
        switch (updateType) {

            case X:
                float x = (float) values[0];
                object.setX(x);
                break;
            case Y:
                float y = (float) values[0];
                object.setY(y);
                break;
            case Z:
                float z = (float) values[0];
                object.setZ(z);
                break;
            case XY:
                float x2 = (float) values[0];
                float y2 = (float) values[1];
                object.setX(x2);
                object.setY(y2);
                break;
            case XYZ:
                float x3 = (float) values[0];
                float y3 = (float) values[1];
                float z3 = (float) values[2];
                object.setPosition(x3, y3, z3);
                break;
            case POSITION_ORIGIN:
                Origin positionOrigin = (Origin) values[0];
                object.setPositionOrigin(positionOrigin);
                break;
            case ANGLE:
                float angle = (float) values[0];
                object.setAngle(angle);
                break;
            case ROTATE_ORIGIN:
                Origin rotateOrigin = (Origin) values[0];
                object.setRotateOrigin(rotateOrigin);
                break;
            case SCALE_X:
                float scaleX = (float) values[0];
                object.setScaleX(scaleX);
                break;
            case SCALE_Y:
                float scaleY = (float) values[0];
                object.setScaleY(scaleY);
                break;
            case SCALE_XY:
                float scaleX2 = (float) values[0];
                float scaleY2 = (float) values[1];
                object.scale(scaleX2, scaleY2);
                break;
            case SCALE_ORIGIN:
                Origin scaleOrigin = (Origin) values[0];
                object.setScaleOrigin(scaleOrigin);
                break;
        }

        object.refreshModel();

    }

}

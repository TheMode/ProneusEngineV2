package fr.proneus.engine.motable.object;

import fr.proneus.engine.graphic.Sprite;
import fr.proneus.engine.graphic.Texture;

import java.util.UUID;

public class GameObject extends Sprite {

    private UUID uniqueId;
    private String identifier;

    public GameObject(Texture texture, UUID uniqueId) {
        super(texture);
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}

package fr.proneus.engine.graphic;

public class Camera {

    // TODO position

    public boolean isPartiallyVisible(Sprite sprite) {
        return sprite.interacts(0, 0) || sprite.interacts(1, 0) || sprite.interacts(0, 1) || sprite.interacts(1, 1);
    }

    public boolean isFullyVisible(Sprite sprite) {
        return sprite.interacts(0, 0) && sprite.interacts(1, 0) && sprite.interacts(0, 1) && sprite.interacts(1, 1);
    }
}

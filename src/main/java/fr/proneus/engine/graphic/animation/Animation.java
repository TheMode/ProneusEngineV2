package fr.proneus.engine.graphic.animation;

import java.util.HashMap;
import java.util.Map;

public class Animation {

    public Map<Integer, AnimationFrame> frames;

    public Animation() {
        this.frames = new HashMap<>();
    }

    public Animation(AnimationFrame... frames) {
        this();
        for (int i = 0; i < frames.length; i++) {
            this.frames.put(i, frames[i]);
        }

    }

    public void append(AnimationFrame frame) {
        int length = frames.size();
        this.frames.put(length, frame);
    }
}

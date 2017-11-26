package fr.proneus.engine.graphic.animation;

import java.util.HashMap;
import java.util.Map;

public class Animation {
	
	public Map<Integer, AnimationFrame> frames = new HashMap<>();
	
	public Animation(AnimationFrame... frames) {
		for(int i=0;i<frames.length;i++){
            this.frames.put(i, frames[i]);
        }
		
	}
	
	

}

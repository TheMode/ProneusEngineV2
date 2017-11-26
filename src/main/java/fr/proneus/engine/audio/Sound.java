package fr.proneus.engine.audio;

import static org.lwjgl.openal.AL10.*;

public class Sound {
	
	private int sound;
	private int id;
	
	public Sound(int sound) {
		this.sound = sound;
		id = alGenSources();
	}
	
	public void play() {
		stop();
		alSourcei(id, AL_BUFFER, sound);
		alSourcePlay(id);
	}
	
	public void setLooping(boolean loop) {
		alSourcei(id, AL_LOOPING, loop ? AL_TRUE:AL_FALSE);
	}
	
	public void pause() {
		alSourcePause(id);
	}
	
	public void resume() {
		alSourcePlay(id);
	}
	
	public void stop() {
		alSourceStop(id);
	}
	
	public boolean isPlaying() {
		return alGetSourcei(id, AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	public void setVelocity(float x, float y) {
		alSource3f(id, AL_VELOCITY, x, y, 0);
	}
	
	public void setVolume(float volume) {
		alSourcef(id, AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		alSourcef(id, AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z) {
		alSource3f(id, AL_POSITION, x, y, z);
	}
	
	public void delete() {
		stop();
		alDeleteSources(id);
	}
}
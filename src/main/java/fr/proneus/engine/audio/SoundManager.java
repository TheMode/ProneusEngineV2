package fr.proneus.engine.audio;

import org.lwjgl.openal.ALCCapabilities;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL.createCapabilities;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC.createCapabilities;
import static org.lwjgl.openal.ALC.destroy;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundManager {
    private static List<Integer> buffers = new ArrayList<>();

    private static long device, context;

    public void init() {
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL)
            throw new IllegalStateException("Loading default openAL device failed.");
        ALCCapabilities caps = createCapabilities(device);

        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL)
            throw new IllegalStateException("Failed to create an OpenAL context.");

        alcMakeContextCurrent(context);
        createCapabilities(caps);
    }

    public void setListenerData(float x, float y, float z) {
        alListener3f(AL_POSITION, x, y, -1);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public int loadSound(InputStream stream) {
        WaveData waveFile = WaveData.create(stream);
        return loadBuffer(waveFile);
    }

    private int loadBuffer(WaveData waveFile) {
        int buffer = 0;
        buffers.add(buffer);

        buffer = alGenBuffers();
        alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public void delete() {
        for (int b : buffers) {
            alDeleteBuffers(b);
        }

        alcDestroyContext(context);
        alcCloseDevice(device);
        destroy();
    }
}
package fr.proneus.engine.utils;

public class Cooldown {

    private long time;

    private long startTime;

    public Cooldown(long time) {
        this.time = time;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public long getRemainingTime() {
        return -Math.min(0, System.currentTimeMillis() - startTime - time);
    }

    public boolean finished() {
        return System.currentTimeMillis() - startTime >= time;
    }

}

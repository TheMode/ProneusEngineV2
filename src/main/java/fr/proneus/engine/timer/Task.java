package fr.proneus.engine.timer;

public class Task {

	public long time;
	public boolean started;

	private int id;

	private Runnable runnable;
	private int firstRun;
	private int schedule;

	public Task(int id, Runnable runnable, int firstRun, int schedule) {
		this.time = System.currentTimeMillis();
		
		this.started = false;

		this.id = id;
		this.runnable = runnable;
		this.firstRun = firstRun;
		this.schedule = schedule;
	}

	public int getID() {
		return id;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public int getFirstRun() {
		return firstRun;
	}

	public int getScheduleTime() {
		return schedule;
	}

}

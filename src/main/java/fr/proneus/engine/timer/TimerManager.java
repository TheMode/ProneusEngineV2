package fr.proneus.engine.timer;

public class TimerManager {

	private Timer timer;

	public TimerManager() {
		this.timer = new Timer();
	}

	public void update() {
		timer.update();
	}

	public Timer getTimer() {
		return timer;
	}
	
	public void reset(){
		timer.reset();
	}
}

package fr.proneus.engine.timer;

public class TimerRunnable implements Runnable{
	
	private TimerManager timerManager;
	private volatile boolean exit = false;
    
	public TimerRunnable(TimerManager timerManager) {
		this.timerManager = timerManager;
	}
	
    public void run() {
        while(!exit){
            timerManager.update();
        }
        
    }
    
    public void stop(){
        exit = true;
    }

}

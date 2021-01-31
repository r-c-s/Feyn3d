package rcs.feyn.utils;

public class AnimationTimer implements Runnable {
	
	private Runnable runnable;
	
	private long interval;
	
	private long last;
	
	public AnimationTimer(Runnable runnable, long millis) {
		this.runnable = runnable;
		this.interval = millis;
	}

	public void run() {
  	long now = System.currentTimeMillis();
  	if (now - last >= interval) {
  		last = now;
  		runnable.run();
  	}
  }
}
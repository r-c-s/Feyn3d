package rcs.feyn.utils;

public class ThreadArray {
  
  private Thread[] threads;
  
  private int index = 0;

  public ThreadArray(int cap) {
    threads = new Thread[cap];
  }

  public void runTask(Runnable r) {
    runTask(r, index++);
  }

  public Thread runTask(Runnable r, int i) {
    if (i >= threads.length) {
      throw new IllegalArgumentException();
    }
    Thread thread = threads[i];
    if (null != thread && thread.isAlive()) {
      throw new IllegalThreadStateException();
    }
    thread = new Thread(r);
    thread.start();
    return thread;
  }

  public void joinAll() {
    for (Thread t : threads) {
      if (t == null) {
        continue;
      }
      try {
        t.join();
      } 
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    index = 0;
  }
}
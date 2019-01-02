package hello.Therad;

import java.util.concurrent.TimeUnit;

public class SleepingTask implements Runnable{
    protected int countDown=10;
    private static int taskCount=0;
    private final int id=taskCount++;

    public SleepingTask(int countDown) {
        this.countDown = countDown;
    }

    public SleepingTask() {
    }
    public String status() {
        return "#"+id+"("+(countDown>0?countDown:"ListOff!")+"),";
    }
    @Override
    public void run() {
        // Thread.yield();
        try {
            while(countDown-->0){
                System.out.print(status());
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

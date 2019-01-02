package hello.Therad;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {
    @Override
    public void run() {
        try {
            while (true){
                TimeUnit.MILLISECONDS.sleep(40);
                System.out.println(Thread.currentThread() + " "+this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println("This is should always run?");
        }
    }
}

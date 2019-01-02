package hello.Therad.Interrupt;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting {
    private static ExecutorService exec=Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException {
        Future<?> f=exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
      //  System.out.println("Interrupting "+r.getClass().getName());
        f.cancel(true);
      //  System.out.println("Interrupt sent to "+r.getClass().getName());
    }
    public static void test1()throws Exception{
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        test(new ReentranLockBlocked());
    }
    public static void test2(){
        ExecutorService executorService=Executors.newCachedThreadPool();
        executorService.execute(new SleepBlocked());
        executorService.execute(new IOBlocked(System.in));
        executorService.execute(new SynchronizedBlocked());
        executorService.execute(new ReentranLockBlocked());
        executorService.shutdownNow();
    }
    public static void main(String[] args) throws Exception {
        test1();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Aborting with System.exit(0)");
        System.exit(0);
    }
}
class SleepBlocked implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("Interrupted from SleepBlocked");
        }
        System.out.println("Exiting SleepBlocked.run()");
    }
}
class IOBlocked implements Runnable{
   private InputStream in;

    public IOBlocked(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {

        try {
            System.out.println("Waiting for read():");
            in.read();
        } catch (IOException e) {
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Interrupted from blocked I/O");
            }else {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Exiting IOBlocked.run()");
    }
}
class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while(true)
            Thread.yield();
    }

    public SynchronizedBlocked() {
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting SynchronizedBlocked.run()");
    }

}
class ReentranLockBlocked implements Runnable{
    private Lock lock=new ReentrantLock();
    public void f(){
        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            System.out.println("Interrupted from ReentranLockBlocked");
        }
    }

    public ReentranLockBlocked() {
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exiting ReentranLockBlocked.run()");
    }

}
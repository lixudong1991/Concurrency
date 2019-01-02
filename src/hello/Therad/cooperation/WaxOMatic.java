package hello.Therad.cooperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaxOMatic {
    public static void main(String[] args) throws InterruptedException {
        Car car=new Car1();
        ExecutorService exec=Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
class Car{
    protected boolean waxOn=false;
    public synchronized void waxed(){
        waxOn=true;
        notifyAll();
    }
    public synchronized void buffed(){
        waxOn=false;
        notifyAll();
    }
    public synchronized void waitForWaxing() throws InterruptedException {
        while(waxOn==false) wait();
    }
    public synchronized void waitForBuffing() throws InterruptedException {
        while (waxOn==true) wait();
    }

}
class WaxOn implements Runnable{
    private Car car;

    public WaxOn(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                System.out.println("Wax On!");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax On task");
    }
}
class  WaxOff implements Runnable{
    private Car car;

    public WaxOff(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                car.waitForWaxing();
                System.out.println("Wax OFF");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting via interrupt");
        }
        System.out.println("Ending Wax off task");
    }
}
class Car1 extends Car{
    private Lock lock=new ReentrantLock();
    private Condition condition=lock.newCondition();
    public  void waxed(){
        lock.lock();
        try {
            waxOn= true;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    public  void buffed(){
        lock.lock();
        try {
            waxOn = false;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    public void waitForWaxing() throws InterruptedException {
        lock.lock();
        try{
            while(waxOn==false)
                condition.await();
        }finally {
            lock.unlock();
        }

    }
    public void waitForBuffing() throws InterruptedException {
        lock.lock();
        try{
            while(waxOn==true)
                condition.await();
        }finally {
            lock.unlock();
        }
    }

}
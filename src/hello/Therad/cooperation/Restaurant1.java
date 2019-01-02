package hello.Therad.cooperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant1 {
    Meal meal;
    ExecutorService exec=Executors.newCachedThreadPool();
    WaitPerson1 waitPerson=new WaitPerson1(this);
    Chef1 chef=new Chef1(this);

    public Restaurant1() {
        exec.execute(waitPerson);
        exec.execute(chef);
    }

    public static void main(String[] args) {
        new Restaurant1();
    }

}
class WaitPerson1 implements Runnable{
    private Restaurant1 restaurant;
    public Lock lock=new ReentrantLock();
    public Condition condition=lock.newCondition();
    public WaitPerson1(Restaurant1 restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                lock.lock();
                try
                {
                    while(restaurant.meal==null)
                        condition.await();
                }finally {
                    lock.unlock();
                }
                System.out.println("WaitPerson1 got "+restaurant.meal);
                restaurant.chef.lock.lock();
                try{
                    restaurant.meal=null;
                    restaurant.chef.condition.signalAll();
                }finally {
                    restaurant.chef.lock.unlock();
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Chef1 implements Runnable{
    private Restaurant1 restaurant;
    private int count=0;
    public Lock lock=new ReentrantLock();
    public Condition condition=lock.newCondition();
    public Chef1(Restaurant1 restaurant) {
        this.restaurant = restaurant;

    }

    @Override
    public void run() {

        try{
            while(!Thread.interrupted()){
                lock.lock();
                try
                {
                    while(restaurant.meal!=null)
                        condition.await();
                }finally {
                    lock.unlock();
                }
                if(++count==10){
                    System.out.println("Out of food,closing");
                    restaurant.exec.shutdownNow();
                }
                System.out.println("Chef1 Order up ");
                restaurant.waitPerson.lock.lock();
                try{
                    restaurant.meal=new Meal(count);
                    restaurant.waitPerson.condition.signalAll();
                }finally {
                    restaurant.waitPerson.lock.unlock();
                }
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

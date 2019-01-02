package hello.Therad.cooperation;


import hello.Therad.ListOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class TestBlockingQueues {
    static void getKey(){
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static void getKey(String message){
        System.out.println(message);
        getKey();
    }
    static void test(String msg,BlockingQueue<ListOff> queue){
        System.out.println(msg);
        ListOffRunner runner=new ListOffRunner(queue);
        Thread t=new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++) {
            runner.add(new ListOff(5));
        }
        getKey("Press Enter ( "+msg+" )");
        t.interrupt();
        System.out.println("Finished "+msg+" test");
    }

    static void test1(String msg,BlockingQueue<ListOff> queue){
       System.out.println(msg);
       ListOffRunner runner=new ListOffRunner(queue);
       ExecutorService exec=Executors.newCachedThreadPool();
       exec.execute(runner);
       exec.execute(new PutListOff(runner));
       getKey("Press Enter ( "+msg+" )");
       exec.shutdownNow();
       System.out.println("Finished "+msg+" test1");
    }

    public static void main(String[] args) {
        test1("LinkedBlockingQueue",new LinkedBlockingQueue<ListOff>());
        test1("ArrayBlockingQueue",new ArrayBlockingQueue<ListOff>(3));
        test1("SynchronousQueue",new SynchronousQueue<ListOff>());
    }
}

class ListOffRunner implements Runnable{
    private BlockingQueue<ListOff> rockets;

    public ListOffRunner(BlockingQueue<ListOff> rockets) {
        this.rockets = rockets;
    }
    public void add(ListOff lo){
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                    ListOff rocket=rockets.take();
                    rocket.run();
            }
        } catch (InterruptedException e) {
            System.out.println("Waking from take()");
        }
        System.out.println("Exiting ListOffRunner");
    }
}
class PutListOff implements Runnable{
    ListOffRunner runner;

    public PutListOff(ListOffRunner runner) {
        this.runner = runner;
    }
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                runner.add(new ListOff(2));
            }
        }finally {
            System.out.println("Exiting PutListOff");
        }
    }
}

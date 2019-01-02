package hello.Therad;

import hello.Therad.Factory.DaemonThreadFactory;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args)  {
        new Main().test5();
        System.out.println("Waiting for ListOff");
    }
    //无返回值的线程
    public void test1() {
        // ExecutorService exec=Executors.newCachedThreadPool();
        // ExecutorService exec=Executors.newFixedThreadPool(5);//预先创建5个线程
        ExecutorService exec=Executors.newSingleThreadExecutor();//预先创建单一线程
        for(int i=0;i<5;i++){
            exec.execute(new ListOff());
        }
        exec.shutdown();
    }
    //有返回值的线程
    public void test2() {
        ExecutorService exec=Executors.newCachedThreadPool();
        ArrayList<Future<String>> results=new ArrayList<Future<String>>();
        for(int i=0;i<10;i++){
            results.add(exec.submit(new TaskWithResult(i)));
        }
        for(Future<String> fs:results)
            try {
                System.out.println(fs.get());
            }catch (InterruptedException e){
                e.printStackTrace();
                return;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                exec.shutdown();
            }

    }
    //线程休眠
    public void test3() {
        ExecutorService exec=Executors.newCachedThreadPool();
        for(int i=0;i<5;i++){
            exec.execute(new SleepingTask());
        }
        exec.shutdown();
    }
    //线程优先级
    public void test4() {
        ExecutorService exec=Executors.newCachedThreadPool();
        for(int i=0;i<5;i++){
            exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));
        }
        exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
        exec.shutdown();
    }
    //创建主线程结束时就会自动结束的线程,这样的线程中finally子句程序不一定会被执行
    public void test5() {
        for(int i=0;i<10;i++){
            Thread t=new Thread(new SimpleDaemons());
            t.setDaemon(true);
            t.start();
        }
        System.out.println("All daemons started");
        try {
            TimeUnit.MILLISECONDS.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //创建主线程结束时就会自动结束的线程,这样的线程中创建的线程也会在主线程结束时就会结束
    public void test6() throws InterruptedException {
        Thread t=new Thread(new Daemons());
        t.setDaemon(true);
        t.start();
        System.out.println("t.isDaemon() = "+t.isDaemon());
        TimeUnit.SECONDS.sleep(1);
    }
    //使用线程工厂设置线程属性，指定Executor创建这样的线程
    public void test7() {
        ExecutorService exec=Executors.newCachedThreadPool(new DaemonThreadFactory());
        for(int i=0;i<10;i++){
            exec.execute(new SimpleDaemons());
        }
        System.out.println("All daemons started");
        try {
            TimeUnit.MILLISECONDS.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

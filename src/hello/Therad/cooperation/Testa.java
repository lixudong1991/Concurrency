package hello.Therad.cooperation;


import java.util.concurrent.*;


public class Testa implements Runnable {
    Testb test;
    int waitsec;
    public Testa(Testb testb,int waitsec) {
        this.test = testb;
        this.waitsec=waitsec;
    }

    @Override
    public void run() {
            try {
                while(!Thread.interrupted()) {
                    TimeUnit.SECONDS.sleep(waitsec);
                    synchronized (test){
                        test.notifyAll();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
         System.out.println("Tesa exit");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService exec=Executors.newCachedThreadPool();
        //Future<?> t=  exec.submit(new Testb(1));
        Testb t=new Testb(1);
        exec.execute(t);
        exec.execute(new Testa(t,1));
        TimeUnit.SECONDS.sleep(20);
        exec.shutdownNow();
    }
}

class Testb implements Runnable{
    int id;
    public Testb(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized (this) {
                    this.wait();
                }
                System.out.println("Helloworld by "+id+" Thread");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Tesb exit");
    }

}
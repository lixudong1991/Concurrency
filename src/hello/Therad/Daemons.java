package hello.Therad;

public class Daemons implements Runnable{
    Thread[] ts=new Thread[10];
    @Override
    public void run() {
        for(int i=0;i<10;i++){
            ts[i]=new Thread(new DaemonSpawn());
            ts[i].start();
            System.out.println("DaemonSpawn "+ i+" started, ");
        }
        for(int i=0;i<ts.length;i++){
            System.out.println("ts[ "+i+"].isDaemon() = "+ts[i].isDaemon()+" ,");
        }
        while (true)
            Thread.yield();
    }
}
class DaemonSpawn implements Runnable{
    @Override
    public void run() {
        while (true) Thread.yield();
    }
}
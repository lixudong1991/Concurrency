package hello.Therad.SharedResouce.two;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
    private ReentrantLock lock=new ReentrantLock();
    public boolean untimed(){
        boolean captured =lock.tryLock();
        try {
            System.out.println("trylock();" +captured);
            return captured;
        }finally {
            if(captured)
                lock.unlock();
        }
    }
    public boolean timed(){
        boolean captured =false;
        try{
            captured=lock.tryLock(2,TimeUnit.SECONDS);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        try{
            System.out.println("tryLock(2,TimeUnit.SECONDS):"+ captured);
            return captured;
        }finally {
            if(captured)
                lock.unlock();
        }

    }

    public static void main(String[] args) {
        final AttemptLocking al=new AttemptLocking();
        new Thread(){
            {
                setDaemon(true);
            }
            @Override
            public void run() {
                while(true) {
                    if(al.lock.tryLock()) {
                        System.out.println(" Thread acquired");
                        return;
                    }
                }
            }
        }.start();
        Thread.yield();
        while(al.untimed()||al.timed()) ;
    }
}

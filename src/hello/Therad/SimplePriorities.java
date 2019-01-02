package hello.Therad;

public class SimplePriorities implements Runnable {
    int countDown=10;
    volatile double d;
    int priority;

    public SimplePriorities(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return  Thread.currentThread()+ " : " + countDown ;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while(true){
            for(int i=1;i<100000;i++){
                d+=(Math.PI+Math.E)/(double)i;
                if(i%1000==0) Thread.yield();
            }
            System.out.println(this);
            if(--countDown == 0 )return;
        }

    }
}

package hello.Therad.JoinThread;

public class Sleeper extends Thread {
    private int duration;
    public Sleeper(String name,int sleep){
        super(name);
        duration=sleep;
        start();
    }
    public void run(){
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.out.println(getName()+" was interrupted. "+ "isInterrupted(); "+ isInterrupted());
            return;
        }
        System.out.println(getName()+" has awakened");
    }
}

package hello.Therad;

public class ListOff implements Runnable {
    protected int countDown=10;
    private static int taskCount=0;
    private final int id=taskCount++;

    public ListOff(int countDown) {
        this.countDown = countDown;
    }

    public ListOff() {
    }
    public String status() {
        return "#"+id+"("+(countDown>0?countDown:"ListOff!")+"),";
    }
    @Override
    public void run() {
        while(countDown-->0){
            System.out.println(status());
            Thread.yield();
        }
    }
}

package hello.Therad.SharedResouce.one;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable {
    private IntGenerator intGenerator;
    private final int id;

    public EvenChecker(IntGenerator intGenerator, int id) {
        this.intGenerator = intGenerator;
        this.id = id;
    }

    @Override
    public void run() {
        while(!intGenerator.isCanceled()) {
            int v=intGenerator.next();
            if(v%2!=0)
            {
                System.out.println(v + " not even!");
                intGenerator.cancel();
            }
        }
    }
    public static void test(IntGenerator intGenerator,int count){
        ExecutorService executorService=Executors.newCachedThreadPool();
        for(int i=0;i<count;i++) {
            executorService.execute(new EvenChecker(intGenerator,i));
        }
        executorService.shutdown();
    }
    public static void test(IntGenerator intGenerator){
       test(intGenerator,10);
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGeneratorSync());
    }
}

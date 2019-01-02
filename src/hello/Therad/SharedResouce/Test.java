package hello.Therad.SharedResouce;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test implements Runnable {
    TestA testA;
    public Test(TestA testA) {
        this.testA = testA;
    }

    @Override
    public void run() {
        int mx=1000;
        while(true){
            int n=testA.getNum();
            mx--;
          if(n!=10)
          {
              System.out.println("num = "+n);
              return;
          }
          if(mx==0)
          {
              System.out.println("mx = "+mx);
              return;
          }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService=Executors.newCachedThreadPool();
        TestA testA=new TestA();
        for (int i=0;i<10;i++){
            executorService.execute(new Test(testA));
        }
        executorService.shutdown();
    }
}
class TestA{
    private int num=10;
    public synchronized int getNum() {
        ++num;
        --num;
        return num ;
    }
}
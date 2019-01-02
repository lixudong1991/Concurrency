package hello.Therad.SharedResouce.one;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EvenGeneratorLock extends EvenGenerator{
    private int currentEvenValue=0;
    private Lock lock=new ReentrantLock();
    @Override
    public  int next() {
        lock.lock();
        try {
            ++currentEvenValue;
            ++currentEvenValue;
            return currentEvenValue;
        }finally {
            lock.unlock();
        }
    }

}

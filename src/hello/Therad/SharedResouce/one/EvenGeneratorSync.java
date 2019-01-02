package hello.Therad.SharedResouce.one;

public class EvenGeneratorSync extends EvenGenerator{
    private int currentEvenValue=0;
    @Override
    public synchronized int next() {
        ++currentEvenValue;
        ++currentEvenValue;
        return currentEvenValue;
    }

}

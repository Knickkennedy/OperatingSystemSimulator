import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {
    private Lock lock;

    public Semaphore(){
        this.lock = new ReentrantLock();
    }

    public void lock(){
        lock.lock();
    }

    public void unlock(){
        lock.unlock();
    }
}

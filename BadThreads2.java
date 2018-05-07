import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yakov on 4/25/2018.
 */
public class BadThreads2 {

    static String message;
    static ReentrantLock lock = new ReentrantLock();
    private static class CorrectorThread
            extends Thread {

        public void run() {
            lock.lock();
            try {
                sleep(3000);
            } catch (InterruptedException e) {}
            // Key statement 1:
            message = "Mares do eat oats.";
            lock.unlock();
        }
    }

    public static void main(String args[])
            throws InterruptedException {
        (new CorrectorThread()).start();
        lock.lock();
        message = "Mares do not eat oats.";
        Thread.sleep(1000);

        // Key statement 2:
        System.out.println(message);
        lock.unlock();

    }

}
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yakov on 4/25/2018.
 */
public class BadThreads {

    static String message;
    static ReentrantLock lock = new ReentrantLock();

    private static class CorrectorThread
            extends Thread {

        public void run() {

            try {
                sleep(2000);
            } catch (InterruptedException e) {
            }
            // Key statement 1:
            lock.lock();
            message = "Mares do eat oats.";
            lock.unlock();
        }
    }

    public static void main(String args[])
            throws InterruptedException {
        lock.lock();
        (new CorrectorThread()).start();
        message = "Mares do not eat oats.";
        Thread.sleep(1000);
        lock.unlock();
        // Key statement 2:
        System.out.println(message);


    }
}
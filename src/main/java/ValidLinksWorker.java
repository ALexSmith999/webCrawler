import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class ValidLinksWorker {
    static public void processPopulatedRawQueue (BlockingQueue<QueueItem> validQueue,
                                         BlockingQueue<QueueItem> databaseQueue,
                                         ExecutorService pool, int size){
        Runnable task = () -> {
            while (true) {
                try {
                    QueueItem item = validQueue.take();
                    System.out.println(item.message() + "The link is checked to populate the Database Queue");
                    databaseQueue.put(item);
                } catch (InterruptedException e) {

                }
            }
        };
        for (int i = 0; i < size; i++) {
            pool.submit(task);
        }
    }
}


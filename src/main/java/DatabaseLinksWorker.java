import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class DatabaseLinksWorker {
    static public void processCheckedQueue (BlockingQueue<QueueItem> databaseQueue,
                                         ExecutorService pool, int size){
        Runnable task = () -> {
            while (true) {
                try {
                    QueueItem item = databaseQueue.take();
                    System.out.println(item.message() + "The link is deserialized and is saved to the Database");
                } catch (InterruptedException e) {

                }
            }
        };
        for (int i = 0; i < size; i++) {
            pool.submit(task);
        }
    }
}


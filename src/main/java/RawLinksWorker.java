import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class RawLinksWorker {
    static public void processRawQueue (BlockingQueue<QueueItem> rawQueue,
                                         BlockingQueue<QueueItem> validQueue,
                                         ExecutorService pool, int size){
        Runnable task = () -> {
            while (true) {
                try {
                    QueueItem item = rawQueue.take();
                    validQueue.put(item);
                    System.out.println(item.message() + "  The link is traversed to populate the Raw Queue");
                } catch (InterruptedException e) {

                }
            }
        };
        for (int i = 0; i < size; i++) {
            pool.submit(task);
        }
    }
}

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class ValidLinksWorker {
    static public void processPopulatedRawQueue (BlockingQueue<ValidQueueItem> validQueue,
                                         BlockingQueue<DbQueueItem> databaseQueue,
                                         ExecutorService pool, int size){
        Runnable task = () -> {
            while (true) {
                try {
                    ValidQueueItem item = validQueue.take();
                    ObjectMapper om = new ObjectMapper();
                    String json;
                    try {
                        json = om.writeValueAsString(item);
                        databaseQueue.put(new DbQueueItem(item.link(), json));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to Deserialize String to Json", e);
                    }
                } catch (InterruptedException | RuntimeException e)  {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
        for (int i = 0; i < size; i++) {
            pool.submit(task);
        }
    }
}


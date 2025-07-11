import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class ValidLinksWorker {
    static public void processPopulatedRawQueue (BlockingQueue<ValidQueueItem> validQueue,
                                         BlockingQueue<DbQueueItem> databaseQueue,
                                         ExecutorService pool, int size, Logger logger){
        Runnable task = () -> {
            while (true) {
                try {
                    ValidQueueItem item = validQueue.take();
                    ObjectMapper om = new ObjectMapper();
                    String json;
                    try {
                        json = om.writeValueAsString(item);
                        logger.debug("A valid json has been obtained : {}",  item.link());
                        databaseQueue.put(new DbQueueItem(item.link(), json));
                    } catch (JsonProcessingException e) {
                        logger.warn("Cannot obtain a valid Json {}: ",  item.link());
                        throw new RuntimeException("Failed to serialize String to Json", e);
                    }
                } catch (InterruptedException | RuntimeException e)  {
                    logger.warn("There is an error while getting a Json representation " +
                            "/ populating the database queue ");
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


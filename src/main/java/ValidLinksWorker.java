import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class ValidLinksWorker {
    /*
    ValidLinksWorker
        - Consumes the valid Queue
        - creates a JSON file from a record item
        - Adds a new message to the database queue
     * */
    static public void processPopulatedRawQueue (BlockingQueue<ValidQueueItem> validQueue,
                                         BlockingQueue<DbQueueItem> databaseQueue,
                                         ExecutorService pool, int size, Logger logger){
        Runnable task = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ValidQueueItem item = validQueue.take();
                    ObjectMapper om = new ObjectMapper();
                    String json;
                    try {
                        json = om.writeValueAsString(item);
                        logger.debug("A valid json has been obtained : {}",  item.link());
                        databaseQueue.put(new DbQueueItem(item.link(), json));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } catch (InterruptedException e) {
                    logger.warn("ValidLinksWorker interrupted. Exiting ..");
                    Thread.currentThread().interrupt();
                    break;
                } catch (RuntimeException e) {
                    logger.warn("Error while creating a Json file");
                }
            }
        };
        for (int i = 0; i < size; i++) {
            pool.submit(task);
        }
    }
}


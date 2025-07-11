import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class DatabaseLinksWorker {
    static public void processCheckedQueue (BlockingQueue<DbQueueItem> databaseQueue, Logger logger){
        Runnable writer = () -> {
            try (BufferedWriter writerOut = new BufferedWriter(new FileWriter("src/main/resources/output.txt", true))) {
                while (true) {
                    DbQueueItem line = databaseQueue.take();
                    //writerOut.write(line.json());
                    writerOut.write(line.link());
                    writerOut.newLine();
                    writerOut.flush();
                    logger.debug("The link {} has been loaded", line.link());
                }
            } catch (IOException | InterruptedException e) {
                logger.warn("Database is inaccessible");
                Thread.currentThread().interrupt();
            }
        };
        new Thread(writer, "FileWriterThread").start();
    }
}


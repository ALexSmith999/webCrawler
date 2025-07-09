import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class DatabaseLinksWorker {
    static public void processCheckedQueue (BlockingQueue<DbQueueItem> databaseQueue){
        Runnable writer = () -> {
            try (BufferedWriter writerOut = new BufferedWriter(new FileWriter("src/main/resources/output.txt", true))) {
                while (true) {
                    DbQueueItem line = databaseQueue.take();
                    //writerOut.write(line.json());
                    writerOut.write(line.link());
                    writerOut.newLine();
                    writerOut.flush();
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Writer failed: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
        new Thread(writer, "FileWriterThread").start();
    }
}


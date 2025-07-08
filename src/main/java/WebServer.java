import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebServer {

    static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
    static BlockingQueue<QueueItem> rawLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<QueueItem> validLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<QueueItem> databaseQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args){

        if (args.length == 0) {
            throw new IllegalStateException("Port should be provided");
        }
        if (args.length == 1) {
            System.out.println("Default number of threads will be used");
        }
        else {
            NUM_OF_THREADS = Integer.parseInt(args[1]);
            System.out.printf("A number of threads per each pool is %s", args[1]);
        }

        ExecutorService mainPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService rawPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService validLinksPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService databasePool = Executors.newFixedThreadPool(NUM_OF_THREADS);

        RawLinksWorker.processRawQueue(rawLinksQueue, validLinksQueue, rawPool, NUM_OF_THREADS);
        ValidLinksWorker.processPopulatedRawQueue(validLinksQueue, databaseQueue, validLinksPool, NUM_OF_THREADS);
        DatabaseLinksWorker.processCheckedQueue(databaseQueue, databasePool, NUM_OF_THREADS);

        try (ServerSocket server = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Socket client = server.accept();
                mainPool.execute(new Initialization(client, rawLinksQueue));
            }
        } catch (IOException e) {
        }
    }
}

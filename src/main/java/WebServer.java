import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebServer {

    static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
    static int DEPTH = Integer.MAX_VALUE;
    static BlockingQueue<RawQueueItem> rawLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<ValidQueueItem> validLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<DbQueueItem> databaseQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args){

        if (args.length == 0) {
            throw new IllegalStateException("Port should be provided");
        }
        NUM_OF_THREADS = args.length < 2 ? NUM_OF_THREADS : Integer.parseInt(args[1]);
        DEPTH = args.length < 3 ? DEPTH : Integer.parseInt(args[2]);
        System.out.printf("A number of threads per each pool is %s, " +
                "the depth of a hierarchy tree is %s", NUM_OF_THREADS, DEPTH);
        System.out.println();

        ExecutorService mainPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService rawPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService validLinksPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        //ExecutorService databasePool = Executors.newFixedThreadPool(NUM_OF_THREADS);

        RawLinksWorker.processRawQueue(rawLinksQueue, validLinksQueue, rawPool, NUM_OF_THREADS, DEPTH);
        ValidLinksWorker.processPopulatedRawQueue(validLinksQueue, databaseQueue, validLinksPool, NUM_OF_THREADS);
        DatabaseLinksWorker.processCheckedQueue(databaseQueue);

        try (ServerSocket server = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Socket client = server.accept();
                mainPool.execute(new Initialization(client, rawLinksQueue));
            }
        } catch (IOException e) {
        }
    }
}

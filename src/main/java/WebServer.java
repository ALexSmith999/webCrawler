import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer {

    static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
    static int DEPTH = Integer.MAX_VALUE;
    static int HTTP_RESPONSE_TIME_OUT = 10;

    static BlockingQueue<RawQueueItem> rawLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<ValidQueueItem> validLinksQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<DbQueueItem> databaseQueue = new LinkedBlockingQueue<>();
    static BlockingQueue<String> seen = new LinkedBlockingQueue<>();
    private static final Logger logger = LogManager.getLogger(WebServer.class);

    public static void main(String[] args){

        if (args.length == 0) {
            logger.error("The port number Should be strictly provided");
            throw new IllegalStateException("Port should be provided");
        }

        NUM_OF_THREADS = args.length < 2 ? NUM_OF_THREADS : Integer.parseInt(args[1]);
        DEPTH = args.length < 3 ? DEPTH : Integer.parseInt(args[2]);
        HTTP_RESPONSE_TIME_OUT = args.length < 4 ? HTTP_RESPONSE_TIME_OUT : Integer.parseInt(args[3]);
        AppLaunch.start(logger, NUM_OF_THREADS, DEPTH, HTTP_RESPONSE_TIME_OUT);

        ExecutorService mainPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService rawPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService validLinksPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        //ExecutorService databasePool = Executors.newFixedThreadPool(NUM_OF_THREADS);

        RawLinksWorker.processRawQueue(rawLinksQueue, validLinksQueue, seen, rawPool,
                NUM_OF_THREADS, DEPTH, logger, HTTP_RESPONSE_TIME_OUT);
        ValidLinksWorker.processPopulatedRawQueue(validLinksQueue, databaseQueue, validLinksPool,
                NUM_OF_THREADS, logger);
        DatabaseLinksWorker.processCheckedQueue(databaseQueue, logger);

        try (ServerSocket server = new ServerSocket(Integer.parseInt(args[0]))) {
            while (true) {
                Socket client = server.accept();
                mainPool.execute(new Initialization(client, rawLinksQueue,
                        HTTP_RESPONSE_TIME_OUT));
            }
        } catch (IOException e) {
            logger.error("It is impossible to allocate server's resources");
            mainPool.close();
            rawPool.close();
            validLinksPool.close();
        }
    }
}

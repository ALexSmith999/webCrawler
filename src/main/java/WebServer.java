import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer {
    /*
    Webserver class :
    - Initializes the projects by receiving main parameters as an input.
    - creates thread pools
        There are three thread pools for different purposes:
            RawPool manages the rawQueue that contains raw links
            ValidPool manages the validQueue, which contains filtered links
            DatabasePool manages the DatabaseQueue that contains links prepared for a load.
    - manages a correct exit (addShutdownHook), closing all resources and threads
    - launches all workers (RawLinksWorker, ValidLinksWorker, DatabaseLinksWorker)
    - launches a Server Socket that listens for incoming requests
    - Thread - safe (concurrent utils are used like nonblocking queues and hashmaps)
    **/

    static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors(); //number of threads(workers) per thread pool, default — number of cores
    static int DEPTH = 3;//how many steps from a parent link to a child is supposed to be done
    static int HTTP_RESPONSE_TIME_OUT = 10;//for how long to wait for a response
    static int MAX_QUEUE_SIZE = 10000;//limits a value to avoid memory leaks

    static BlockingQueue<RawQueueItem> rawLinksQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    static BlockingQueue<ValidQueueItem> validLinksQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    static BlockingQueue<DbQueueItem> databaseQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    static Set<String> seen = ConcurrentHashMap.newKeySet();
    private static final Logger logger = LogManager.getLogger(WebServer.class);

    public static void main(String[] args){

        if (args.length == 0) {
            logger.error("The port number Should be strictly provided");
            throw new IllegalStateException("Port should be provided");
        }

        NUM_OF_THREADS = args.length < 2 ? NUM_OF_THREADS : Integer.parseInt(args[1]);
        DEPTH = args.length < 3 ? DEPTH : Integer.parseInt(args[2]);
        HTTP_RESPONSE_TIME_OUT = args.length < 4 ? HTTP_RESPONSE_TIME_OUT : Integer.parseInt(args[3]);
        MAX_QUEUE_SIZE = args.length < 5 ? MAX_QUEUE_SIZE : Integer.parseInt(args[4]);
        AppLaunch.start(logger, NUM_OF_THREADS, DEPTH, HTTP_RESPONSE_TIME_OUT, MAX_QUEUE_SIZE);

        ExecutorService mainPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService rawPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        ExecutorService validLinksPool = Executors.newFixedThreadPool(NUM_OF_THREADS);
        //ExecutorService databasePool = Executors.newFixedThreadPool(NUM_OF_THREADS);

        RawLinksWorker.processRawQueue(rawLinksQueue, validLinksQueue, seen, rawPool,
                NUM_OF_THREADS, DEPTH, logger, HTTP_RESPONSE_TIME_OUT);
        ValidLinksWorker.processPopulatedRawQueue(validLinksQueue, databaseQueue, validLinksPool,
                NUM_OF_THREADS, logger);
        DatabaseLinksWorker.processCheckedQueue(databaseQueue, logger, seen);

        //starts server socket
        ServerSocket server;
        try {
            server = new ServerSocket(Integer.parseInt(args[0]));
        } catch (IOException e) {
            logger.error("Could not start server");
            return;
        }

        //exit from the application, closing all server's resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown initiated...");
            try {
                server.close();
            } catch (IOException e) {
                logger.warn("Failed to close server socket.");
            }

            rawPool.shutdownNow();
            validLinksPool.shutdownNow();
            mainPool.shutdownNow();

            try {
                rawPool.awaitTermination(5, TimeUnit.SECONDS);
                validLinksPool.awaitTermination(5, TimeUnit.SECONDS);
                mainPool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            AppLaunch.stop(logger);
        }));

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = server.accept();
                //instantiates the mandatory class
                mainPool.execute(new Initialization(client, rawLinksQueue, HTTP_RESPONSE_TIME_OUT));
            }
        } catch (IOException e) {
            logger.error("Server loop terminated.");
        }
    }
}

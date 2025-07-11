import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Initialization implements Runnable {
    /*
    Class is responsible for :
    - Creating a two-way communication channel to receive requests
      and send short acknowledgments
    - The protocol suggests sending a shutdown message (-1) to track closing messages
    - Adds the head to the raw Queue
    **/

    private final Socket client;
    private static final Logger logger = LogManager.getLogger(Initialization.class);
    BlockingQueue<RawQueueItem> queue;
    private int httpResponseTimeOut;
    private int maxRetries;

    Initialization (Socket client, BlockingQueue<RawQueueItem> rawLinksQueue,
                    int httpResponseTimeOut){
        this.client = client;
        this.queue = rawLinksQueue;
        this.httpResponseTimeOut = httpResponseTimeOut;
        this.maxRetries = maxRetries;
    }

    @Override
    public void run() {
        try (InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int currVal;
            while ((currVal = in.read()) != -1) {
                buffer.write(currVal);
            }

            String response = "Request is being processed";
            byte[] dataToBeSend = response.getBytes(StandardCharsets.UTF_8);
            out.write(dataToBeSend);
            client.shutdownOutput();

            String link = buffer.toString(StandardCharsets.UTF_8);
            if (ValidationChecks.linkIsValid(link, httpResponseTimeOut, logger)) {
                queue.put(new RawQueueItem(link, 1));
                logger.info("Link has been added to the Main Queue : {}", link);
            }
            else {
                logger.info("Link is invalid : {}", link);
            }

        } catch (IOException | InterruptedException e) {
            logger.warn("There is an error while sending " +
                    "/ receiving messages via the network connection");
            Thread.currentThread().interrupt();
        }
    }
}

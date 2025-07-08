import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class Initialization implements Runnable {
    private final Socket client;
    BlockingQueue<QueueItem> queue;
    Initialization (Socket client, BlockingQueue<QueueItem> rawLinksQueue){
        this.client = client;
        this.queue = rawLinksQueue;
    }

    @Override
    public void run() {
        try (InputStream in = client.getInputStream(); OutputStream out = client.getOutputStream()){
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int currVal;
            while ((currVal = in.read()) != -1){
                buffer.write(currVal);
            }

            String response = "Request is being processed";
            byte[] dataToBeSend = response.getBytes(StandardCharsets.UTF_8);
            out.write(dataToBeSend);
            client.shutdownOutput();

            queue.put(new QueueItem(buffer.toString(StandardCharsets.UTF_8), 1));

        } catch (IOException | InterruptedException e) {
        }
    }
}

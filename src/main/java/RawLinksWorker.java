import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class RawLinksWorker {
    static public void processRawQueue (BlockingQueue<RawQueueItem> rawQueue,
                                         BlockingQueue<ValidQueueItem> validQueue,
                                         ExecutorService pool, int size, int DEPTH){
        Runnable task = () -> {
            while (true) {
                try {
                    RawQueueItem item = rawQueue.take();
                    Document doc;
                    if (item.level() >= DEPTH) {
                        doc = AppHtmlResponse.returnDoc(item);
                        validQueue.put(new ValidQueueItem(item.message(), doc.toString()));
                        continue;
                    }
                    doc = AppHtmlResponse.returnDoc(item);
                    Elements links = doc.getElementsByTag("a");
                    for (var link : links) {
                        String next = link.attr("abs:href");
                        if (!ValidationChecks.linkIsValid(next)) {
                            continue;
                        }
                        rawQueue.put(new RawQueueItem(next, item.level() + 1));
                    }
                    validQueue.put(new ValidQueueItem(item.message(), doc.toString()));

                } catch (InterruptedException | IOException e) {
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

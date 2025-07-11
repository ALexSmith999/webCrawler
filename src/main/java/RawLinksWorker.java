import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class RawLinksWorker {
    static public void processRawQueue (BlockingQueue<RawQueueItem> rawQueue,
                                        BlockingQueue<ValidQueueItem> validQueue,
                                        BlockingQueue<String> seen, ExecutorService pool, int size, int DEPTH,
                                        Logger logger, int httpResponseTimeOut){
        Runnable task = () -> {
            while (true) {
                try {
                    RawQueueItem item = rawQueue.take();
                    seen.put(item.message());

                    Document doc;
                    if (item.level() >= DEPTH) {
                        doc = AppHtmlResponse.returnDoc(item, httpResponseTimeOut);
                        validQueue.put(new ValidQueueItem(item.message(), doc.toString()));
                        logger.debug("The final depth has been reached, " +
                                "no further processing for the next link : {}", item.message());
                        continue;
                    }

                    doc = AppHtmlResponse.returnDoc(item, httpResponseTimeOut);
                    logger.debug("The document has been extracted " +
                            "for the link : {}", item.message());

                    Elements links = doc.getElementsByTag("a");
                    for (var link : links) {
                        String next = link.attr("abs:href");
                        if (!ValidationChecks.linkIsValid(next, httpResponseTimeOut, logger)) {
                            continue;
                        }
                        logger.debug("The child link {} has been put " +
                                "into the raw Queue" +
                                "for the link : {}", next, item.message());
                        if (seen.contains(next)) {
                            continue;
                        }
                        rawQueue.put(new RawQueueItem(next, item.level() + 1));
                    }
                    validQueue.put(new ValidQueueItem(item.message(), doc.toString()));

                } catch (InterruptedException | IOException e) {
                    logger.warn("There is an error while consuming the rawQueue" +
                            " or parsing a html document");
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

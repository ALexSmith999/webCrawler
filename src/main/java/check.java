import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class check {
    /*public static void main(String[] args) {
        Logger logger = LogManager.getLogger(check.class);
        String reference = "https://books.toscrape.com";

        System.out.println(ValidationChecks.linkIsValid(reference, 10, logger));
        try {
            Document doc = AppHtmlResponse.returnDoc(new RawQueueItem(reference, 1), 10);
            Elements links = doc.getElementsByTag("a");
            for (var link : links) {
                String next = link.attr("abs:href");
                if (!ValidationChecks.linkIsValid(next, 10, logger)) {
                    continue;
                }
                System.out.println(next);
            }
        } catch (IOException | InterruptedException e) {
        }
    }*/
}

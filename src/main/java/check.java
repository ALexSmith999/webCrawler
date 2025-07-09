import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class check {
    /*public static void main(String[] args) throws IOException, InterruptedException {
        String initiallink = "https://www.transfermarkt.com";
        RawQueueItem item = new RawQueueItem(initiallink, 1);

        Document doc = AppHtmlResponse.returnDoc(item);
        //System.out.println("Reponse doc : " + doc);
        Elements links = doc.getElementsByTag("a");
        for (var link : links) {
            String next = link.attr("abs:href");
            if (!ValidationChecks.linkIsValid(next)) {
                continue;
            }
            System.out.println(next);
        }

    }*/
}

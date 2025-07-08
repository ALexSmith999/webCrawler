import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class HtmlParser {
    /*public record ref(String value, int level) {}
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        Queue<ref> queue = new LinkedList<>();
        ArrayList<String> sites = new ArrayList<>(List.of(
                "https://www.transfermarkt.com"
                , "https://fbref.com/en/"
                , "https://footystats.org/"));
        for (var site : sites) {
            queue.add(new ref(site, 1));}

        HttpClient client = HttpClient.newBuilder().build();
        while (!queue.isEmpty()){
            ref currentRef = queue.poll();
            if (currentRef.level > 5) {
                continue;
            }
            System.out.print(currentRef.value + "\n");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(currentRef.value))
                    .build();
            HttpResponse<String> resp
                    = client.send(request, HttpResponse.BodyHandlers.ofString());
            Thread.sleep(2000);

            Document doc = Jsoup.parse(resp.body(), currentRef.value);
            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                String next = link.attr("abs:href");
                if (!(next.startsWith("http") || next.startsWith("https"))
                        || next.isEmpty()) {
                    continue;
                }
                System.out.println( next);
            }
            //CONVERT TO JSON
            Person person = new Person("mkyong", 42);

            ObjectMapper om = new ObjectMapper();

            try {

                // covert Java object to JSON strings
                String json = om.writeValueAsString(person);

                // output: {"name":"mkyong","age":42}
                System.out.println(json);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }*/
}
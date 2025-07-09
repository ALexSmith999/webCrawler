import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AppHtmlResponse {
    public static Document returnDoc(RawQueueItem item) throws IOException, InterruptedException {
        HttpClient client = AppHttpClient.INSTANCE.getClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(item.message()))
                .timeout(java.time.Duration.ofSeconds(2))
                .build();
        HttpResponse<String> resp
                = client.send(request, HttpResponse.BodyHandlers.ofString());
        return Jsoup.parse(resp.body(), item.message());
    }
}

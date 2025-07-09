import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ValidationChecks {
    static public boolean linkIsValid(String link){
        if (!link.startsWith("http")) {
            return false;
        }
        HttpClient client = AppHttpClient.INSTANCE.getClient();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(link))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(java.time.Duration.ofSeconds(2))
                    .build();

            HttpResponse<Void> response = client.send(request
                    , HttpResponse.BodyHandlers.discarding());
            int code = response.statusCode();
            return code >= 200 && code < 400;

        } catch (Exception e) {
            return false;
        }
    }
    static public boolean linkIsSuccessor(String link, String previous){
        return link.contains(previous);
    }
}

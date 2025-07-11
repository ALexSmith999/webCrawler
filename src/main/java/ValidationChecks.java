import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ValidationChecks {
    static public boolean linkIsValid(String link, int httpResponseTimeOut, Logger logger){

        if (!link.startsWith("http")) {
            return false;
        }
        int code = 500;
        try {
            HttpClient client = AppHttpClient.INSTANCE.getClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(link))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(java.time.Duration.ofSeconds(httpResponseTimeOut))
                    .build();
            HttpResponse<Void> response = client.send(request
                    , HttpResponse.BodyHandlers.discarding());
            code = response.statusCode();
            return isResponseValid(code);
        } catch (InterruptedException | IOException e) {
            logger.warn("An error while checking {}: ", link);
            Thread.currentThread().interrupt();
            return false;
        }
    }
    static public boolean isResponseValid(int status){
        return status >= 200 && status < 400;
    }
    static public boolean linkIsSuccessor(String link, String previous){
        return link.contains(previous);
    }

}

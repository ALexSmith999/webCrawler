import java.net.http.HttpClient;

public enum AppHttpClient {
    INSTANCE;
    private final HttpClient client;
    AppHttpClient(){
        client = HttpClient
                .newBuilder().followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }
    public HttpClient getClient (){
        return client;
    }
}

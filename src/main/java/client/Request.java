package client;

import java.util.ArrayList;
import java.util.List;

public class Request {
    public static void main(String[] args) {
        String host = "vm1";
        int port = 1300;
        ArrayList<String> messages = new ArrayList<>(List.of(
                "https://books.toscrape.com"
        ));
        for (int i = 0; i < messages.size(); i++) {
            Thread th = new Thread(new RequestBody(messages.get(i), host, port));
            th.start();
        }
    }
}

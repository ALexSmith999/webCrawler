package server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import org.bson.*;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import server.WebServer;
import server.utils.Encryption;
import server.utils.ProjectVariables;
import server.utils.ValidationChecks;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Sorts.descending;

public class test {
    /*public static void main(String[] args) throws NoSuchAlgorithmException {
        String link = "https://quotes.toscrape.com/author/Albert-Einstein";
        System.out.println(new Encryption(link).returnSHA1());

        String link1 = "https://quotes.toscrape.com/author/Charles-Bukowski";
        System.out.println(new Encryption(link).returnSHA1());
        System.out.println(new Encryption(link1).returnSHA1());
        String str = "https://quotes.toscrape.com";

        //"https://books.toscrape.com";
        //"https://quotes.toscrape.com/"
        //"https://sandbox.oxylabs.io/products"
        //"https://www.scrapethissite.com/pages/"

        StringBuilder uri = new StringBuilder("mongodb://");
        uri.append("vm");
        uri.append(":");
        uri.append(27017);

        MongoClient mongoclient = MongoClients.create(uri.toString());
        MongoDatabase DB = mongoclient.getDatabase("loads");

        MongoCollection<Document> collection
                = DB.getCollection(ProjectVariables.COLLECTION.label);

        Document existing = collection.find(eq("uid"
                        , "26ba5d403cd8ee6191c37fe397820b4328bc18f0"))
                .sort(descending("version"))
                .limit(1)
                .first();

        String str1 = (String) existing.get("content");
        org.jsoup.nodes.Document html = Jsoup.parse(str1);
        Element mainDiv = html.selectFirst("div.author-details");
        //System.out.println(mainDiv);
        if (mainDiv != null) {

            Element author = mainDiv.selectFirst("h3.author-title");
            Element birth = mainDiv.selectFirst("span.author-born-date");
            Element loc = mainDiv.selectFirst("span.author-born-location");
            Element desc = mainDiv.selectFirst("div.author-description");

            System.out.println(author.text() + " " + birth.text() + " " + loc.text());
            System.out.println("\n");
            System.out.println(desc.text());
        }


        for (var item : collection.find()) {
            String str1 = (String) item.get("link");
            if (str1.startsWith(str)) {
                Object body = item.get("content");
                System.out.println((String) body);
            }
        }
    }*/
}

package Workshop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main01 {
    public static void main(String[] args) {
        String URL_ADDRESS = "https://www.infoworld.com/";

        Document doc = null;
        try {
            doc = Jsoup.connect(URL_ADDRESS + "category/java/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select("div.article h3 a");

        List<String> listUrl = elements.stream()
                .map(element -> element.toString().substring(10))
                .toList();

        List<String> listFileTitles = elements.stream()
                .map(element -> element.toString().substring(26).replaceAll("html", "txt"))
                .toList();

        String[] arrayUrl = listUrl.toArray(String[]::new);
        String[] arrayFileTitles = listFileTitles.toArray(String[]::new);

        for (int i = 0; i < arrayUrl.length; i++) {
            String[] parts = arrayUrl[i].split("\">");
            arrayUrl[i] = parts[0];
            String[] parts1 = arrayFileTitles[i].split("\">");
            arrayFileTitles[i] = parts1[0];
        }
        ExecutorService executorService = Executors.newFixedThreadPool(20);
                for (int i = 0; i < arrayUrl.length; i++) {
                    int counter = i;
                    executorService.execute(() -> {
                        System.out.println(Thread.currentThread().getName());
                        new FileSaver().saveToFile(counter, arrayUrl, arrayFileTitles);
                    });
                }
                executorService.shutdown();
    }
}

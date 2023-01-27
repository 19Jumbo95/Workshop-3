package Test;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main01 {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println("Data rozpoczęcia: " + dateTime);
        String URL_ADDRESS = "https://www.infoworld.com/";

        // Pobieranie strony głównej i danych konkretnych artykułów
        Document doc = null;
        try {
            doc = Jsoup.connect(URL_ADDRESS + "category/java/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select("div.article h3 a");

        //Tworzenie listy z nazwami artykułów zmapowanymi do wersji pozwalającej pobrać artykuły oraz zapisanie ich do tablicy stringów
        List<String> list = elements.stream()
                .map(element -> element.toString().substring(10))
                .toList();
        String[] array = list.toArray(String[]::new);
        for (int i = 0; i < array.length; i++) {
            String[] parts = array[i].split("\">");
            array[i] = parts[0];
        }

        //Tworzenie listy z nazwami artykułów zmapowanymi do wersji pozwalającej stworzyć nazwę pliku oraz zapisanie ich do tablicy stringów
        List<String> list1 = elements.stream()
                .map(element -> element.toString().substring(26).replaceAll("html", "txt"))
                .toList();
        String[] arrayFileTitle = list1.toArray(String[]::new);
        for (int i = 0; i < arrayFileTitle.length; i++) {
            String[] parts = arrayFileTitle[i].split("\">");
            arrayFileTitle[i] = parts[0];
        }

        //Mechanizm zapisywania plików
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < array.length; i++) {
                    Document doc1 = null;
                    try {
                        doc1 = Jsoup.connect(URL_ADDRESS + array[i]).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Elements elements1 = doc1.select("div[id=drr-container]");
                    String text = elements1.text();
                    UUID uuid = UUID.randomUUID();
                    String uuidToString = UUID.randomUUID().toString();
                    String fileTitle = uuidToString + "-" + arrayFileTitle[i];
                    File file = new File(fileTitle);
                    try {
                        Random random = new Random();
                        FileUtils.writeStringToFile(file, text);
                        System.out.println("Plik został zapisany" + i);
                        //Thread.sleep(random.nextInt(10)*100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } /*catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }*/
                }
                LocalDateTime dateTime1 = LocalDateTime.now();
                System.out.println("Data zakończenia: " + dateTime1);
            }

        });
        executorService.shutdown();
    }
}

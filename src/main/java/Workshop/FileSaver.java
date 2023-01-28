package Workshop;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileSaver{
    String URL_ADDRESS = "https://www.infoworld.com/";
    public void saveToFile(int i, String[] arr, String[]arr1) {
        Document doc1 = null;
        try {
            doc1 = Jsoup.connect(URL_ADDRESS + arr[i]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements1 = doc1.select("div[id=drr-container]");
        File file = new File(UUID.randomUUID().toString() + "-" + arr1[i]);
        try {
            FileUtils.writeStringToFile(file, elements1.text());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

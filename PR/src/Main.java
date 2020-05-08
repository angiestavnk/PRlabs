import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        Content content = new Content(80,"unite.md");
        String data = content.getContent();
        ArrayDeque<String> myList = new ArrayDeque<>();

        System.out.println(data);
        Pattern pattern = Pattern.compile("[a-zA-Z0-9\\/]+.(png|jp[e]*g|gif)");
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            myList.add(data.substring(matcher.start(), matcher.end()));
        }

        ImageDownloader imageDownloader = new ImageDownloader(myList);
        imageDownloader.downloadImages();
    }
}

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageDownloader {
    Semaphore threadsSemaphore;
    Semaphore imageSemaphore;
    ArrayDeque<String> imagesList;

    public ImageDownloader(ArrayDeque<String> imagesList) {
        this.threadsSemaphore = new Semaphore(4);
        this.imageSemaphore = new Semaphore(1);
        this.imagesList = imagesList;
    }

    private class MyThread extends Thread {
        String name;
        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                threadsSemaphore.acquire();//enter semaphore
                try {
                    while (!imagesList.isEmpty()) {
                        imageSemaphore.acquire();
                        String imageLink = imagesList.pop();
                        imageSemaphore.release();//semaphore is free
                        downloadImage(imageLink);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                finally {
                    threadsSemaphore.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void downloadImage(String link) throws InterruptedException, IOException {
            InetAddress host = InetAddress.getByName("unite.md");
            Socket socket = new Socket(host, 80);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            wr.write("GET " + link + " HTTP/1.1\r\n");
            wr.write("Host: unite.md\r\n");
            wr.write("Accept-Language: en, ru, ro\r\n");
            wr.write("Connection: keep-alive\r\n");
            wr.write("Keep-Alive: 300\r\n");
            wr.write("Cache-Control: no-cache\r\n");
            wr.write("Accept-Charset: utf-8, iso-8859-1;q=0.5\r\n");
            wr.write("\r\n");
            wr.flush();

            Pattern pattern = Pattern.compile("[a-zA-Z0-9.]+((.)jpg|(.)png)");
            Matcher matcher = pattern.matcher(link);
            while (matcher.find()) {
                link = link.substring(matcher.start(), matcher.end());
            }

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            OutputStream outputStream = new FileOutputStream(link);
            int count, offset;
            byte[] buffer = new byte[8096];
            boolean eohFound = false;
            while ((count = dataInputStream.read(buffer)) != -1)
            {
                offset = 0;
                if (!eohFound)
                {
                    String string = new String(buffer, 0, count);
                    int indexOfEOH = string.indexOf("\r\n\r\n");
                    if (indexOfEOH != -1)
                    {
                        count = count - indexOfEOH - 4;
                        offset = indexOfEOH + 4;
                        eohFound = true;
                    }
                    else
                    {
                        count = 0;
                    }
                }
                outputStream.write(buffer, offset, count);
                outputStream.flush();
            }

            dataInputStream.close();
            outputStream.close();
            wr.close();
            socket.close();
            System.out.println("Image was downloaded on thread: " + MyThread.currentThread().getName());
        }
    }

    public void downloadImages() {
        MyThread thread = new MyThread("thread1");
        MyThread thread1 = new MyThread("thread2");
        MyThread thread2 = new MyThread("thread3");
        MyThread thread3 = new MyThread("thread4");
        MyThread thread4 = new MyThread("thread5");
        MyThread thread5 = new MyThread("thread6");

        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}


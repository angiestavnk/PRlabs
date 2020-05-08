import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Content {
    Integer port;
    String host;

    public Content(Integer port, String host) {
        this.port = port;
        this.host = host;
    }

    public String getContent() throws IOException{
        InetAddress host = InetAddress.getByName(this.host);
        Client client = new Client(host, this.port);
        System.out.println(client.socket);

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.socket.getOutputStream(), "UTF8"));
        wr.write("GET / HTTP/1.1\r\n");
        wr.write("Host: unite.md\r\n");
        wr.write("Accept-Language: en, ru, ro\r\n");
        wr.write("Connection: keep-alive\r\n");
        wr.write("Keep-Alive: 300\r\n");
        wr.write("Cache-Control: no-cache\r\n");
        wr.write("\r\n");

// Send parameters
        wr.flush();

// Get response
        BufferedReader rd = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
        String line;

        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (rd.ready()) {
                int i = 0;
                while (i != -1) {
                    i = rd.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        line = sb.toString();
        wr.close();
        rd.close();


        client.socket.close();
        return line;




    }
}

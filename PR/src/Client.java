import java.net.*;
import java.io.*;

public class Client {
    public Socket socket;

    public Client (InetAddress address, int port) throws IOException {
        socket = new Socket(address, port);
    }
}

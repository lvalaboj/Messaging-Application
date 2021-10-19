import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket sc = null;
        try {
            sc = new ServerSocket(4242);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                assert sc != null;
                Socket s = sc.accept();
                
                Communicator chat = new Communicator(s);
                new Thread(chat).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

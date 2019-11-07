import java.net.Socket;

public class ClientHandler extends Thread {
    Socket client;
    public ClientHandler(Socket clientSocket){
        client = clientSocket;
    }

    @Override
    public void run() {
        super.run();

    }
}

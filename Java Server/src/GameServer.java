import java.io.IOException;
import java.net.Socket;

public class GameServer extends Thread{
    Socket player1;
    Socket player2;


    public GameServer(Socket player1, Socket player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        try {
            player1.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Thread running with two socket connections");
    }
}

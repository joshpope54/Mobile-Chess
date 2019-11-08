import java.io.IOException;
import java.net.Socket;

public class GameServer extends Thread{
    ClientHandler player1;
    ClientHandler player2;


    public GameServer(ClientHandler player1, ClientHandler player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        System.out.println("outputing from gameserver - p1:" + player1.client  + "\np2:"+player2.client);
        System.out.println("Thread running with two socket connections");
    }
}

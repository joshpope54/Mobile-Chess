import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        //Connection Made,
        //Inform Both Sockets
        try {
            DataOutputStream playerOneOutput = new DataOutputStream(player1.client.getOutputStream());
            DataOutputStream playerTwoOutput = new DataOutputStream(player2.client.getOutputStream());

            playerOneOutput.writeUTF("connected");
            System.out.println("Player 1 sent - Communications sent" + player1.client);
            playerTwoOutput.writeUTF("connected");
            System.out.println("Player 2 sent - Communications sent" + player2.client);

            //send game state to clients

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

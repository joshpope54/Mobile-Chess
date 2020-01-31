
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class GameClientHandler {
    Socket client;
    Scanner dataInputStream;
    PrintWriter dataOutputStream;
    String string;

    public GameClientHandler(Socket clientSocket, String type){
        client = clientSocket;
        string = type;
        try {
            dataInputStream = new Scanner(clientSocket.getInputStream());
            dataOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void run() {
//        String received;
//        while(true){
//            try{
//
//                received = dataInputStream.readUTF();
//                System.out.println(received);
//
//                if(received.equalsIgnoreCase("exit"))
//                {
//                    System.out.println(string + " Client " + this.client + " sends exit...");
//                    System.out.println(string + " Closing this connection.");
//                    MatchMaker.waitingForPlayers.remove(this);
//                    System.out.println(string + " Removing " + this + " from waiting for players new Count: " + MatchMaker.waitingForPlayers.size());
//                    this.client.close();
//                    System.out.println(string + " Connection closed");
//                    //socket closed
//                    //remove me from the waiting list
//                    break;
//                }else{
//
//                }
//
//
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            this.dataInputStream.close();
//            this.dataOutputStream.close();
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
//    }
}

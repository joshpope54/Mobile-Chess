import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class Server {
    public static void main(String[] args) {
        MatchMaker matchMaker = new MatchMaker();
        matchMaker.start();
    }
}


class MatchMaker extends Thread {
    private static String serverString = "[SERVER] ";
    ServerSocket serverSocket;
    // Constructor
    public MatchMaker() {
        try {
            serverSocket = new ServerSocket(5560);
            System.out.println(serverString + "Started");
            System.out.println(serverString + "Awaiting first client connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true){
                Socket s = null;
                try {
                    s = serverSocket.accept();

                    //Got A player
                    //Add them to to waiting queue (List of Sockets?)
                    //Theres a pair?
                    //Create a game server
                    //With 2 Sockets;
                    //Game Server Class
                    //Holds Player 1 and 2 Socket




                }catch (Exception e){
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
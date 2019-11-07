import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
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
    private static ArrayList<Thread> waitingForPlayers;
    // Constructor
    public MatchMaker() {
        try {
            serverSocket = new ServerSocket(5560);
            waitingForPlayers = new ArrayList<>();
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

                    ClientHandler handler = new ClientHandler(s);
                    waitingForPlayers.add(handler);
                    handler.start();


                    //Create Client handler thread?

                    System.out.println("Connection made by " + handler.client + " queue count: "+ waitingForPlayers.size());

                    //Cant Just hold socket
                    //Needs to be able to be closed if user quits the matchmaking queue
                    //Can only do this on a thread




//                    if(waitingForPlayers.size() % 2 == 0){
//                        Random random = new Random();
//
//                        int random1 = random.nextInt(waitingForPlayers.size());
//                        int random2 = random.nextInt(waitingForPlayers.size());
//
//                        while (random1==random2){
//                            random2 = random.nextInt(waitingForPlayers.size());
//                        }
//                        //System.out.println(random1 + "   " + random2);
//                        //waiting is a even amount
//                        //create a game server with two random players
//                        GameServer game = new GameServer(waitingForPlayers.get(random1), waitingForPlayers.get(random2));
//                        game.start();
//                        waitingForPlayers.remove(random1);
//                        waitingForPlayers.remove(random2);
//                        //removal problem - if random1 < random2 removing random1 will shift the position of random2 such that it is not longer at the same position
//                        System.out.println(waitingForPlayers.size());
//                    }


                    //Got A player - done
                    //Add them to to waiting queue (List of Sockets?) - done
                    //Theres a pair? - done
                    //Create a game server - done
                    //With 2 Sockets; - done
                    //Game Server Class - work on - contains game logic
                    //Holds Player 1 and 2 Socket - uses the sockets to communicate




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

//I need a client handler
//I need to be able to tell a thread to shutdown if the user doesnt want to stay in the matchmaking queue
//Add these threads to a arraylist to how the numbers of players that are playing.
//Number of games in progress?
//Add all games to a count
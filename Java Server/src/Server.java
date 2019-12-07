import chess.Chess;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server extends Thread{
    public static String serverString = "[SERVER] ";
    ServerSocket serverSocket;

    public Server(){
        try {
            serverSocket = new ServerSocket(5561);
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
                    ClientHandler handler = new ClientHandler(s, serverString);
                    handler.start();
                    System.out.println(serverString + "Connection made by " + handler.client);
                }catch (Exception e){
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        MatchMaker matchMaker = new MatchMaker();
//        matchMaker.start();
//        Server server = new Server();
//        server.start();
        Chess chess = new Chess();
        chess.outputBoard();
        chess.chessPieces[1][0].move(chess,3,0);
        chess.outputBoard();
        chess.chessPieces[3][0].move(chess,4,0);
        chess.outputBoard();
        chess.chessPieces[4][0].move(chess,5,0);
        chess.outputBoard();
        chess.chessPieces[5][0].move(chess,6,1);
        chess.outputBoard();
        System.out.println(chess.deadWhitePieces);
        chess.chessPieces[6][1].move(chess,7,2);
        chess.outputBoard();
        System.out.println(chess.deadWhitePieces);
        chess.chessPieces[0][0].move(chess,6,0);
        chess.outputBoard();
        System.out.println(chess.deadWhitePieces);


    }
}


class MatchMaker extends Thread {
    private static String matchmaker = "[MATCHMAKER] ";
    ServerSocket serverSocket;
    public static ArrayList<ClientHandler> waitingForPlayers;
    // Constructor
    public MatchMaker() {
        try {
            serverSocket = new ServerSocket(5560);
            waitingForPlayers = new ArrayList<>();
            System.out.println(matchmaker + "Started");
            System.out.println(matchmaker + "Awaiting first client connection");
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
                    //Once accepted send information about player Id, etc
                    ClientHandler handler = new ClientHandler(s, matchmaker);
                    waitingForPlayers.add(handler);
                    handler.start();


                    //Create Client handler thread?

                    System.out.println(matchmaker + "Connection made by " + handler.client + " queue count: "+ waitingForPlayers.size());

                    //Cant Just hold socket
                    //Needs to be able to be closed if user quits the matchmaking queue
                    //Can only do this on a thread



//array list storted by elo score//players either side are the closest ones
                    if(waitingForPlayers.size() % 2 == 0){
                        Random random = new Random();

                        int random1 = random.nextInt(waitingForPlayers.size());
                        int random2 = random.nextInt(waitingForPlayers.size());

                        while (random1==random2){
                            random2 = random.nextInt(waitingForPlayers.size());
                        }
                        //System.out.println(random1 + "   " + random2);
                        //waiting is a even amount
                        //create a game server with two random players
                        ClientHandler player1 = waitingForPlayers.get(random1);
                        ClientHandler player2 = waitingForPlayers.get(random2);

                        GameServer game = new GameServer(player1, player2);
                        game.start();
                        waitingForPlayers.remove(player1);
                        waitingForPlayers.remove(player2);

                    }


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
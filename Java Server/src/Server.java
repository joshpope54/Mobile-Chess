import com.example.ce301.chess.Chess;

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
        MatchMaker matchMaker = new MatchMaker();
        matchMaker.start();
//        Server server = new Server();
//        server.start();

//        Chess chess = new Chess();
//        chess.outputBoard();
//        chess.chessPieces[1][2].move(chess, 3,2);
//        chess.outputBoard();
//        System.out.println(chess.blackKing.checkIfInCheck(chess));
//        System.out.println(chess.whiteKing.checkIfInCheck(chess));

//        chess.chessPieces[0][0].move(chess, 2,0);
//        chess.outputBoard();
//        System.out.println(chess.blackKing.checkIfInCheck(chess));
//        System.out.println(chess.whiteKing.checkIfInCheck(chess));
//
//        chess.chessPieces[2][0].move(chess, 2,4);
//        chess.outputBoard();
//        System.out.println(chess.blackKing.checkIfInCheck(chess));
//        System.out.println(chess.whiteKing.checkIfInCheck(chess));
//
//        chess.chessPieces[7][5].move(chess, 6,4);
//        chess.outputBoard();
//        System.out.println(chess.blackKing.checkIfInCheck(chess));
//        System.out.println(chess.whiteKing.checkIfInCheck(chess));
//
//        chess.chessPieces[7][3].move(chess, 6,3);
//        chess.outputBoard();
//        System.out.println(chess.blackKing.checkIfInCheck(chess));
//        System.out.println(chess.whiteKing.checkIfInCheck(chess));

    }
}


class MatchMaker extends Thread {
    private static String matchmaker = "[MATCHMAKER] ";
    ServerSocket serverSocket;
    public static ArrayList<GameClientHandler> waitingForPlayers;
    private int totalPlayers;
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
                    GameClientHandler handler = new GameClientHandler(s, matchmaker);
                    handler.start();
                    waitingForPlayers.add(handler);
                    totalPlayers+=1;

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
                        GameClientHandler player1 = waitingForPlayers.get(random1);
                        GameClientHandler player2 = waitingForPlayers.get(random2);
                        GameServer game = new GameServer(player1, player2);
                        game.start();
                        waitingForPlayers.remove(player1);
                        waitingForPlayers.remove(player2);

                    }

                    System.out.println("TOTAL PLAYERS: "+ totalPlayers);

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

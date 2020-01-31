import com.example.ce301.chess.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class GameServer extends Thread{
    private GameClientHandler player1;
    private GameClientHandler player2;
    private Chess chess;


    public GameServer(GameClientHandler player1, GameClientHandler player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public void run() {
        System.out.println("outputing from gameserver - p1:" + player1.client  + "\np2:"+player2.client);
        System.out.println("Thread running with two socket connections");
        //Connection Made,
        //Inform Both Sockets
        PrintWriter playerOneOutput = player1.dataOutputStream;
        PrintWriter playerTwoOutput = player2.dataOutputStream;

        playerOneOutput.println("connected");
        System.out.println("Player 1 sent - Communications sent" + player1.client);
        playerTwoOutput.println("connected");
        System.out.println("Player 2 sent - Communications sent" + player2.client);
        chess = new Chess();

        Random random = new Random();
        int whiteOrBlack = random.nextInt(1);
        if(whiteOrBlack ==1){
            playerOneOutput.println("WHITE");
            playerTwoOutput.println("BLACK");
        }else{
            playerOneOutput.println("BLACK");
            playerTwoOutput.println("WHITE");
        }
        //send game state to clients
        while(chess.gameInProgress){
            if(whiteOrBlack ==1){
                //playerone white
                //playertwo black
                String moves = player1.dataInputStream.nextLine();
                createPosition(moves, player1, playerOneOutput,playerTwoOutput);
                String moves2 = player2.dataInputStream.nextLine();
                createPosition(moves2, player2, playerOneOutput,playerTwoOutput);

            }else{
                String moves = player2.dataInputStream.nextLine();
                createPosition(moves, player2, playerOneOutput,playerTwoOutput);
                String moves2 = player1.dataInputStream.nextLine();
                createPosition(moves2, player1, playerOneOutput,playerTwoOutput);
                //extract information
            }


        }
    }

    public void createPosition(String pieces, GameClientHandler player, PrintWriter outputStreamForPlayer1, PrintWriter outputStreamForPlayer2){
        String[] items = pieces.split(" ");

        System.out.println(Arrays.toString(items));

        String[] firstPostion = items[1].split(",");
        String[] secondPosition = items[2].split(",");

        ChessPiece.PieceColor readyStatus = ChessPiece.PieceColor.valueOf(items[0]);

        if(chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])]!=null && chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].getPieceColor().equals(readyStatus)){
            boolean success = chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].move(chess, Integer.parseInt(secondPosition[0]), Integer.parseInt(secondPosition[1]));
            if(success){
                if(player.equals(player1)){
                    createOutput(outputStreamForPlayer1, outputStreamForPlayer2, firstPostion, secondPosition, player1);
                }else if(player.equals(player2)){
                    createOutput(outputStreamForPlayer1, outputStreamForPlayer2, firstPostion, secondPosition, player2);
                }
            }else{
                    outputStreamForPlayer1.println("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                    outputStreamForPlayer2.println("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            }
        }else{
                outputStreamForPlayer1.println("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                outputStreamForPlayer2.println("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
        }


    }

    private void createOutput(PrintWriter outputStreamForPlayer1, PrintWriter outputStreamForPlayer2, String[] firstPostion, String[] secondPosition, GameClientHandler player) {
        outputStreamForPlayer1.println("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
        outputStreamForPlayer2.println("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);

        if(chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] instanceof Pawn && (Integer.parseInt(secondPosition[0])==0 || Integer.parseInt(secondPosition[0])==7)) {
            //promote piece
            if (player.equals(player1)) {
                outputStreamForPlayer1.println("PROMOTE");
            } else if (player.equals(player2)) {
                outputStreamForPlayer2.println("PROMOTE");
            }
            String pieces = player.dataInputStream.nextLine();
            String[] items = pieces.split(" ");
            System.out.println(Arrays.toString(items));
            String newPieceType = items[1];
            ChessPiece.PieceColor currentColor = chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].getPieceColor();
            Pawn pawn = (Pawn) chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])];
            switch (newPieceType) {
                case "QUEEN":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Queen();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    outputStreamForPlayer1.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " QUEEN");
                    outputStreamForPlayer2.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " QUEEN");
                    break;
                case "KNIGHT":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Knight();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    outputStreamForPlayer1.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " KNIGHT");
                    outputStreamForPlayer2.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " KNIGHT");
                    break;
                case "ROOK":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Rook();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    outputStreamForPlayer1.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " ROOK");
                    outputStreamForPlayer2.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " ROOK");
                    break;
                case "BISHOP":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Bishop();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    outputStreamForPlayer1.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " BISHOP");
                    outputStreamForPlayer2.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " BISHOP");
                    break;
            }
        }
    }
}

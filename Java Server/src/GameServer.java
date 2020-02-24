import com.example.ce301.chess.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class GameServer extends Thread{
    private GameClientHandler player1;
    private GameClientHandler player2;
    private Chess chess;
    PrintWriter playerOneOutput;
    PrintWriter playerTwoOutput;

    public GameServer(GameClientHandler player1, GameClientHandler player2){
        this.player1 = player1;
        this.player2 = player2;
        player1.server=this;
        player2.server=this;
        playerOneOutput = player1.dataOutputStream;
        playerTwoOutput = player2.dataOutputStream;
    }

    public void run() {
        System.out.println("outputing from gameserver - p1:" + player1.client  + "\np2:"+player2.client);
        System.out.println("Thread running with two socket connections");
        //Connection Made,
        //Inform Both Sockets


        chess = new Chess();

        Random random = new Random();
        int whiteOrBlack = random.nextInt(1);
        if(whiteOrBlack ==1){
            playerOneOutput.println("CONNECTION_MADE WHITE");
            playerTwoOutput.println("CONNECTION_MADE BLACK");
        }else{
            playerOneOutput.println("CONNECTION_MADE BLACK");
            playerTwoOutput.println("CONNECTION_MADE WHITE");
        }
        //send game state to clients
        while(player1.isAlive() || player2.isAlive()){
        }
        while(chess.gameInProgress){
            if(whiteOrBlack ==1){
                //playerone white
                //playertwo black
                String moves = player1.dataInputStream.nextLine();
                createPosition(moves, player1);
                String moves2 = player2.dataInputStream.nextLine();
                createPosition(moves2, player2);

            }else{
                String moves = player2.dataInputStream.nextLine();
                createPosition(moves, player2);
                String moves2 = player1.dataInputStream.nextLine();
                createPosition(moves2, player1);
                //extract information
            }


        }
    }

    public void createPosition(String pieces, GameClientHandler player){
        String[] items = pieces.split(" ");

        System.out.println(Arrays.toString(items));

        String[] firstPostion = items[1].split(",");
        String[] secondPosition = items[2].split(",");

        ChessPiece.PieceColor readyStatus = ChessPiece.PieceColor.valueOf(items[0]);

        if(chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])]!=null && chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].getPieceColor().equals(readyStatus)){
            boolean SUCCESS = chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].move(chess, Integer.parseInt(secondPosition[0]), Integer.parseInt(secondPosition[1]));
            if(SUCCESS){
                if(player.equals(player1)){
                    createOutput(firstPostion, secondPosition, player1);
                }else if(player.equals(player2)){
                    createOutput(firstPostion, secondPosition, player2);
                }
            }else{
                playerOneOutput.println("FAILURE "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                playerTwoOutput.println("FAILURE "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            }
        }else{
            playerOneOutput.println("FAILURE "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            playerTwoOutput.println("FAILURE "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
        }


    }

    private void createOutput(String[] firstPostion, String[] secondPosition, GameClientHandler player) {
        playerOneOutput.println("SUCCESS "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
        playerTwoOutput.println("SUCCESS "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);

        if(chess.blackKing.checkIfInCheck(chess) || chess.whiteKing.checkIfInCheck(chess)){
            playerOneOutput.println("FAILURE YOUR_KING_IN_CHECK");
            playerTwoOutput.println("FAILURE YOUR_KING_IN_CHECK");
        }
        if(chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] instanceof Pawn && (Integer.parseInt(secondPosition[0])==0 || Integer.parseInt(secondPosition[0])==7)) {
            //promote piece
            if (player.equals(player1)) {
                playerOneOutput.println("PROMOTE");
            } else if (player.equals(player2)) {
                playerTwoOutput.println("PROMOTE");
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
                    playerOneOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " QUEEN");
                    playerTwoOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " QUEEN");
                    break;
                case "KNIGHT":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Knight();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    playerOneOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " KNIGHT");
                    playerTwoOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " KNIGHT");
                    break;
                case "ROOK":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Rook();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    playerOneOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " ROOK");
                    playerTwoOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " ROOK");
                    break;
                case "BISHOP":
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Bishop();
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(), pawn.getY());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                    chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());
                    playerOneOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " BISHOP");
                    playerTwoOutput.println("PROMOTION " + secondPosition[0] + "," + secondPosition[1] + " " + currentColor + " BISHOP");
                    break;
            }
        }
    }
}

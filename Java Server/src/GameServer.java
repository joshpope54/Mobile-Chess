import com.example.ce301.chess.Chess;
import com.example.ce301.chess.ChessPiece;
import com.example.ce301.chess.Pawn;
import com.example.ce301.chess.Queen;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        try {
            ObjectOutputStream playerOneOutput = new ObjectOutputStream(player1.client.getOutputStream());
            ObjectOutputStream playerTwoOutput = new ObjectOutputStream(player2.client.getOutputStream());

            playerOneOutput.writeObject("connected");
            System.out.println("Player 1 sent - Communications sent" + player1.client);
            playerTwoOutput.writeObject("connected");
            System.out.println("Player 2 sent - Communications sent" + player2.client);
            chess = new Chess();

            Random random = new Random();
            int whiteOrBlack = random.nextInt(1);
            if(whiteOrBlack ==1){
                playerOneOutput.writeObject("WHITE");
                playerTwoOutput.writeObject("BLACK");
            }else{
                playerOneOutput.writeObject("BLACK");
                playerTwoOutput.writeObject("WHITE");
            }
            //send game state to clients
            while(chess.gameInProgress){
                if(whiteOrBlack ==1){
                    //playerone white
                    //playertwo black
                    String moves = player1.dataInputStream.readUTF();
                    createPosition(moves, player1, playerOneOutput,playerTwoOutput);
                    String moves2 = player2.dataInputStream.readUTF();
                    createPosition(moves2, player2, playerOneOutput,playerTwoOutput);

                }else{
                    String moves = player2.dataInputStream.readUTF();
                    createPosition(moves, player2, playerOneOutput,playerTwoOutput);
                    String moves2 = player1.dataInputStream.readUTF();
                    createPosition(moves2, player1, playerOneOutput,playerTwoOutput);
                    //extract information
                }


            }

        } catch (IOException e) {
            System.out.println("Player has disconnected so game over");
        }

    }

    public void createPosition(String pieces, GameClientHandler player, ObjectOutputStream outputStreamForPlayer1, ObjectOutputStream outputStreamForPlayer2){
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
                try {
                    outputStreamForPlayer1.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                    outputStreamForPlayer2.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                outputStreamForPlayer1.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                outputStreamForPlayer2.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void createOutput(ObjectOutputStream outputStreamForPlayer1, ObjectOutputStream outputStreamForPlayer2, String[] firstPostion, String[] secondPosition, GameClientHandler player) {
        try {
            outputStreamForPlayer1.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            outputStreamForPlayer2.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);

            if(chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] instanceof Pawn && (Integer.parseInt(secondPosition[0])==0 || Integer.parseInt(secondPosition[0])==7)){
                //promote piece
                System.out.println("WRITING PROMOTE");
                if(player.equals(player1)){
                    outputStreamForPlayer1.writeObject("PROMOTE");

                }else if(player.equals(player2)){
                    outputStreamForPlayer2.writeObject("PROMOTE");

                }
                String pieces = player.dataInputStream.readUTF();
                System.out.println("READING RESPONSE " + pieces);
                String[] items = pieces.split(" ");
                System.out.println(Arrays.toString(items));
                String newPieceType = items[1];
                ChessPiece.PieceColor currentColor = chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].getPieceColor();

                switch (newPieceType){
                    case "QUEEN":
                        Pawn pawn = (Pawn) chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])];
                        chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] = new Queen();
                        chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPosition(pawn.getX(),pawn.getY());
                        chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceColor(pawn.getPieceColor());
                        chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])].setPieceState(pawn.getPieceState());


                        System.out.println("WRITING RESPONSE");
                        outputStreamForPlayer1.writeObject("PROMOTION "+secondPosition[0]+","+secondPosition[1] + " "+currentColor + " QUEEN");
                        outputStreamForPlayer2.writeObject("PROMOTION "+secondPosition[0]+","+secondPosition[1] + " "+currentColor + " QUEEN");
                        break;
                    case "KNIGHT":
                        break;
                    case "ROOK":
                        break;
                    case "BISHOP":
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

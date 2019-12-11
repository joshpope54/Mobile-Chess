import com.example.ce301.chess.Chess;
import com.example.ce301.chess.ChessPiece;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class GameServer extends Thread{
    GameClientHandler player1;
    GameClientHandler player2;
    Chess chess;
    private int whiteOrBlack;


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
            whiteOrBlack = random.nextInt(1);
            if(whiteOrBlack==1){
                playerOneOutput.writeObject("WHITE");
                playerTwoOutput.writeObject("BLACK");
            }else{
                playerOneOutput.writeObject("BLACK");
                playerTwoOutput.writeObject("WHITE");
            }
            //send game state to clients
            while(chess.gameInProgress){
                if(whiteOrBlack==1){
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
                    try {
                        outputStreamForPlayer1.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                        outputStreamForPlayer2.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(player.equals(player2)){
                    try {
                        outputStreamForPlayer1.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                        outputStreamForPlayer2.writeObject("success "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
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
        }else{
            try {
                outputStreamForPlayer1.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
                outputStreamForPlayer2.writeObject("failure "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

import com.example.ce301.chess.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class GameServer extends Thread{
    public GameClientHandler player1;
    public GameClientHandler player2;
    public Chess chess;
    PrintWriter playerOneOutput;
    PrintWriter playerTwoOutput;
    public boolean currentMovingPlayer; //true for white //false for black
    public String playerOneInput;
    public String playerTwoInput;
    public String player1UserName;
    public String player2UserName;

    public GameServer(GameClientHandler player1, GameClientHandler player2){
        this.player1 = player1;
        this.player2 = player2;
        player1.server=this;
        player2.server=this;
        playerOneOutput = player1.dataOutputStream;
        playerTwoOutput = player2.dataOutputStream;
    }

    public void run() {
        System.out.println("Game Server Started");

        chess = new Chess();
        currentMovingPlayer = true;
        Random random = new Random();
        int whiteOrBlack = random.nextInt(1);
        if(whiteOrBlack ==1){
            player1.playerColor = "WHITE";
            player2.playerColor = "BLACK";
            playerOneOutput.println("CONNECTION_MADE WHITE");
            playerTwoOutput.println("CONNECTION_MADE BLACK");
        }else{
            player1.playerColor = "BLACK";
            player2.playerColor = "WHITE";
            playerOneOutput.println("CONNECTION_MADE BLACK");
            playerTwoOutput.println("CONNECTION_MADE WHITE");
        }
        //REQUIRED TO PREVENT RACE CONDITION
        while(player1.isAlive() || player2.isAlive()){
        }
        playerOneOutput.println("ENEMY_NAME "+player2.userName);
        playerTwoOutput.println("ENEMY_NAME "+player1.userName);
        while(chess.gameInProgress){
            try {
                if (whiteOrBlack == 1) {
                    if (currentMovingPlayer) {
                        String moves = player1.dataInputStream.nextLine();
                        createPosition(moves, player1);
                    } else {
                        String moves2 = player2.dataInputStream.nextLine();
                        createPosition(moves2, player2);
                    }
                } else {
                    if (currentMovingPlayer) {
                        String moves = player2.dataInputStream.nextLine();
                        createPosition(moves, player2);
                    } else {
                        String moves2 = player1.dataInputStream.nextLine();
                        createPosition(moves2, player1);
                    }
                }
            }catch(NoSuchElementException noLine){
                System.out.println("Some client has disconnected");
                try {
                    player1.client.close();
                    player2.client.close();
                } catch (IOException e) {
                }
                chess.gameInProgress = false;
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
            if(currentMovingPlayer){//true if white //false if black
                boolean initialKingState = chess.whiteKing.checkIfInCheck(chess, false);
                Reason SUCCESS = chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].move(chess, Integer.parseInt(secondPosition[0]), Integer.parseInt(secondPosition[1]), 0);
                if (initialKingState){//true if check
                    boolean kingStateAfter = chess.whiteKing.checkIfInCheck(chess, false);
                    if(kingStateAfter){
                        //move invalid
                        //King was in check and still is in check
                        //System.out.println("WHITE King was in check and still is");
                        Reason failureReason = new Reason(false, "CREATES_CHECK");
                        //chess.outputBoard();
                        //System.out.println("Calling UNDO MOVE - from white in check and still is");
                        chess.undoMove();

                        //chess.undoMove(Integer.parseInt(secondPosition[0]),Integer.parseInt(secondPosition[1]),Integer.parseInt(firstPostion[0]),Integer.parseInt(firstPostion[1]));
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, failureReason);

                    }else{
                        //move valid
                        //King was in check, Move means king is not in check
                        //System.out.println("WHITE King was in check and no longer is");
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, SUCCESS);

                    }
                }else{
                    boolean kingStateAfter = chess.whiteKing.checkIfInCheck(chess, false);
                    if(!kingStateAfter){
                        //king was not in check, still not in check , valid
                        //System.out.println("WHITE King was not in check and still is");
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, SUCCESS);

                    }else{
                        //king was not in check, now is in check, invalid
                        Reason failureReason = new Reason(false, "CREATES_CHECK");
                        //System.out.println("WHITE King was not in check and now is");
                        //undo the move?
                        //chess.outputBoard();
                        //System.out.println("Calling UNDO MOVE - from white not check and now is");
                        chess.undoMove();

                        //chess.undoMove(Integer.parseInt(secondPosition[0]),Integer.parseInt(secondPosition[1]),Integer.parseInt(firstPostion[0]),Integer.parseInt(firstPostion[1]));
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, failureReason);

                    }
                }
            }else {
                boolean initialKingState = chess.blackKing.checkIfInCheck(chess, false);
                //System.out.println(initialKingState);
                Reason SUCCESS = chess.chessPieces[Integer.parseInt(firstPostion[0])][Integer.parseInt(firstPostion[1])].move(chess, Integer.parseInt(secondPosition[0]), Integer.parseInt(secondPosition[1]), 0);
                //System.out.println(SUCCESS);
                if (initialKingState){//true if check
                    boolean kingStateAfter = chess.blackKing.checkIfInCheck(chess, false);
                    if(kingStateAfter){
                        //move invalid
                        //King was in check and still is in check
                        //System.out.println("BLACK King was in check and still is");
                        Reason failureReason = new Reason(false, "CREATES_CHECK");
                        //chess.outputBoard();
                        //System.out.println("Calling UNDO MOVE - from black in check and still is");
                        chess.undoMove();

                        //chess.undoMove(Integer.parseInt(secondPosition[0]),Integer.parseInt(secondPosition[1]),Integer.parseInt(firstPostion[0]),Integer.parseInt(firstPostion[1]));
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, failureReason);

                    }else{
                        //move valid
                        //King was in check, Move means king is not in check
                        //S/ystem.out.println("BLACK King was in check and no longer is");
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, SUCCESS);

                    }
                }else{
                    boolean kingStateAfter = chess.blackKing.checkIfInCheck(chess, false);
                    if(!kingStateAfter){
                        //king was not in check, still not in check , valid
                        //System.out.println("BLACK King was not in check and still is");
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, SUCCESS);

                    }else{
                        //king was not in check, now is in check, invalid
                        //System.out.println("BLACK King was not in check and now is");
                        Reason failureReason = new Reason(false, "CREATES_CHECK");
                        //chess.outputBoard();
                        //System.out.println("Calling UNDO MOVE - from black not in check and now is");
                        chess.undoMove();
                        //chess.undoMove(Integer.parseInt(secondPosition[0]),Integer.parseInt(secondPosition[1]),Integer.parseInt(firstPostion[0]),Integer.parseInt(firstPostion[1]));
                        //chess.outputBoard();
                        generatePlayerMoveInfo(player, firstPostion, secondPosition, failureReason);

                    }
                }
            }

        }else{
            chess.outputBoard();
            playerOneOutput.println("FAILURE ");
            playerTwoOutput.println("FAILURE ");
        }
        System.out.println("DEBUG STARTS\n");
        for(Chess.Move move: chess.moves){
            System.out.println(move);
        }
        chess.outputBoard();
        System.out.println("DEBUG ENDS\n\n");

    }

    private void generatePlayerMoveInfo(GameClientHandler player, String[] firstPostion, String[] secondPosition, Reason SUCCESS) {
        if(SUCCESS.isSuccess()){
            switch(SUCCESS.getReason()) {
                case "KING_SIDE_CASTLING":
                    int[][] kingSideRook  = {{Integer.parseInt(firstPostion[0]),7},{Integer.parseInt(firstPostion[0]),5}};
                    if(player.equals(player1)){
                        createCastlingOutput(firstPostion,secondPosition,kingSideRook,player1);
                    }else if(player.equals(player2)){
                        createCastlingOutput(firstPostion,secondPosition,kingSideRook,player2);
                    }
                    currentMovingPlayer = !currentMovingPlayer;
                    break;
                case "QUEEN_SIDE_CASTLING":
                    int[][] queenSideRook  = {{Integer.parseInt(firstPostion[0]),0},{Integer.parseInt(firstPostion[0]),3}};
                    if(player.equals(player1)){
                        createCastlingOutput(firstPostion,secondPosition,queenSideRook,player1);
                    }else if(player.equals(player2)){
                        createCastlingOutput(firstPostion,secondPosition,queenSideRook,player2);
                    }
                    currentMovingPlayer = !currentMovingPlayer;
                    break;
                default:
                    if(player.equals(player1)){
                        createOutput(firstPostion, secondPosition, player1);
                        //System.out.println(chess.blackKing.checkIfInCheck(chess));
                        //System.out.println(chess.whiteKing.checkIfInCheck(chess));
                    }else if(player.equals(player2)){
                        createOutput(firstPostion, secondPosition, player2);
                        //System.out.println(chess.blackKing.checkIfInCheck(chess));
                        //System.out.println(chess.whiteKing.checkIfInCheck(chess));
                    }
                    currentMovingPlayer = !currentMovingPlayer;

            }
        }else{
            switch(SUCCESS.getReason()){
                case "CREATES_CHECK":
                    if(player.equals(player1)){
                        playerOneOutput.println("FAILURE CREATES_CHECK");
                    }else if (player.equals(player2)){
                        playerTwoOutput.println("FAILURE CREATES_CHECK");
                    }
                    break;
                default:
                    if(player.equals(player1)){
                        playerOneOutput.println("FAILURE INVALID_MOVE");
                    }else if (player.equals(player2)){
                        playerTwoOutput.println("FAILURE INVALID_MOVE");
                    }
            }
        }
    }

    private void createCastlingOutput(String[] firstPostion, String[] secondPosition, int[][] rookPieces, GameClientHandler player) {
        playerOneOutput.println("CASTLING "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]+ " "+rookPieces[0][0]+","+rookPieces[0][1]+" "+rookPieces[1][0]+","+rookPieces[1][1]);
        playerTwoOutput.println("CASTLING "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]+ " "+rookPieces[0][0]+","+rookPieces[0][1]+" "+rookPieces[1][0]+","+rookPieces[1][1]);
    }

    private void createOutput(String[] firstPostion, String[] secondPosition, GameClientHandler player) {
        playerOneOutput.println("SUCCESS "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
        playerTwoOutput.println("SUCCESS "+firstPostion[0]+","+firstPostion[1]+" "+secondPosition[0]+","+secondPosition[1]);
//        System.out.println("blakc");
//        System.out.println(chess.blackKing.getX() +""+ chess.blackKing.getY());
//        System.out.println("White");
//        System.out.println(chess.whiteKing.getX() +""+ chess.whiteKing.getY());
        //System.out.println(chess.blackKing);
        //System.out.println(chess.whiteKing);
        Reason blackKingResponse = chess.blackKing.checkIfInCheckMate(chess);
        Reason whiteKingResponse = chess.whiteKing.checkIfInCheckMate(chess);

        if(blackKingResponse.isSuccess()){//true if in check or checkmate
            if(blackKingResponse.getReason().equals("CHECK")){
                playerOneOutput.println("CHECK BLACK");
                playerTwoOutput.println("CHECK BLACK");
            }else if (blackKingResponse.getReason().equals("CHECKMATE")){
                playerOneOutput.println("CHECKMATE BLACK");
                playerTwoOutput.println("CHECKMATE BLACK");
            }
        }else if(whiteKingResponse.isSuccess()){//true if in check or checkmate
            if(whiteKingResponse.getReason().equals("CHECK")){
                playerOneOutput.println("CHECK WHITE");
                playerTwoOutput.println("CHECK WHITE");
            }else if (whiteKingResponse.getReason().equals("CHECKMATE")){
                playerOneOutput.println("CHECKMATE WHITE");
                playerTwoOutput.println("CHECKMATE WHITE");
            }
        }else if(chess.chessPieces[Integer.parseInt(secondPosition[0])][Integer.parseInt(secondPosition[1])] instanceof Pawn && (Integer.parseInt(secondPosition[0])==0 || Integer.parseInt(secondPosition[0])==7)) {
            //promote piece
            if (player.equals(player1)) {
                playerOneOutput.println("PROMOTE");
            } else if (player.equals(player2)) {
                playerTwoOutput.println("PROMOTE");
            }
            String pieces = player.dataInputStream.nextLine();
            String[] items = pieces.split(" ");
            //System.out.println(Arrays.toString(items));
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

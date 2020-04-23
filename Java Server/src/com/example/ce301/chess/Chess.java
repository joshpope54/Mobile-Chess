package com.example.ce301.chess;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Chess implements Serializable{

    //Who is Black?
    //Who is White?
    //Display of grid is client side, Your colour should be closest to you
    //Server holds grid in one state, White at the bottom black at the top
    //So on one client black will be at the bottom, need a way of flipping the moves received from the player.
    //EG player moves piece [7][6] to [5][5] as a black player it should be converted to [0][1] to [2][2]

    //todo: //fix players being able to fill queue with multiple moves - > allow for one additional
    //todo: //finalise chess game, -> finish and close when won/lost -> after 30s?


    /*
    [
    [00,01,02,03,04,05,06,07],
    [10,11,12,13,14,15,16,17],
    [20,21,22,23,24,25,26,27],
    [30,31,32,33,34,35,36,37],
    [40,41,42,43,44,45,46,47],
    [50,51,52,53,54,55,56,57],
    [60,61,62,63,64,65,66,67],
    [70,71,72,73,74,75,76,77]
    ]

    [0][0] top left
    [0][7] top right
    [7][0] bottom left
    [7][7] bottom right

     */
    public static class Move{
        private int startRow;
        private int startCol;
        private int finishRow;
        private int finishCol;
        private ChessPiece capturedPiece;
        public Move(int startRow, int startCol, int finishRow, int finishCol, ChessPiece capturedPiece) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.finishRow = finishRow;
            this.finishCol = finishCol;
            this.capturedPiece = capturedPiece; // null if no piece captured
        }
        public int getStartRow() {
            return startRow;
        }

        public int getStartCol() {
            return startCol;
        }

        public int getFinishRow() {
            return finishRow;
        }

        public int getFinishCol() {
            return finishCol;
        }

        public ChessPiece getCapturedPiece() {
            return capturedPiece;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "startRow=" + startRow +
                    ", startCol=" + startCol +
                    ", finishRow=" + finishRow +
                    ", finishCol=" + finishCol +
                    ", capturedPiece=" + capturedPiece +
                    '}';
        }
    }

    public ChessPiece[][] chessPieces;
    public ArrayList<ChessPiece> deadWhitePieces;
    public ArrayList<ChessPiece> deadBlackPieces;

    public ArrayList<ChessPiece> aliveWhitePieces;
    public ArrayList<ChessPiece> aliveBlackPieces;

    public ArrayList<Move> moves;

    public King blackKing;
    public King whiteKing;
    public boolean gameInProgress;

    public Chess() {
        this.aliveWhitePieces = new ArrayList<>();
        this.aliveBlackPieces = new ArrayList<>();
        this.chessPieces = generateInitalBoard();
        this.deadWhitePieces = new ArrayList<>();
        this.deadBlackPieces = new ArrayList<>();
        this.moves = new ArrayList<>();
        gameInProgress = true;
    }


    public boolean movePiece(int startRow, int startCol, int finishRow, int finishCol, boolean isEnPassant){

        //check if we are enpassant
        if(isEnPassant){
            //yes so we gotta take pawn indirectly
            if(chessPieces[finishRow][finishCol]==null) {
                //enpassnt positions
                if(chessPieces[startRow][startCol].getPieceColor().equals(ChessPiece.PieceColor.WHITE)){
                    //white piece so we gotta check behind us using -
                    int enpassantX = finishRow+1;
                    int enpassantY = finishCol;
                    Move move = new Move(startRow, startCol, finishRow, finishCol, chessPieces[enpassantX][enpassantY]);
                    moves.add(move);
                    chessPieces[enpassantX][enpassantY].setPieceState(ChessPiece.PieceState.DEAD);
                    deadWhitePieces.add(chessPieces[enpassantX][enpassantY]);
                    aliveWhitePieces.remove(chessPieces[enpassantX][enpassantY]);
                    chessPieces[enpassantX][enpassantY] = null;
                    chessPieces[finishRow][finishCol] = chessPieces[startRow][startCol];
                    chessPieces[startRow][startCol] = null;
                    chessPieces[finishRow][finishCol].setPosition(finishRow, finishCol);
                    outputBoard();
                    System.out.println(moves.get(moves.size()-1));
                    return true;
                }else{
                    int enpassantX = finishRow-1;
                    int enpassantY = finishCol;
                    Move move = new Move(startRow, startCol, finishRow, finishCol, chessPieces[enpassantX][enpassantY]);
                    moves.add(move);
                    chessPieces[enpassantX][enpassantY].setPieceState(ChessPiece.PieceState.DEAD);
                    deadBlackPieces.add(chessPieces[enpassantX][enpassantY]);
                    aliveBlackPieces.remove(chessPieces[enpassantX][enpassantY]);
                    chessPieces[enpassantX][enpassantY] = null;
                    chessPieces[finishRow][finishCol] = chessPieces[startRow][startCol];
                    chessPieces[startRow][startCol] = null;
                    chessPieces[finishRow][finishCol].setPosition(finishRow, finishCol);
                    outputBoard();
                    System.out.println(moves.get(moves.size()-1));
                    return true;
                }

            }
        }else{
            if(chessPieces[finishRow][finishCol]!=null){
                //capturing piece
                if(!chessPieces[startRow][startCol].getPieceColor().equals(chessPieces[finishRow][finishCol].getPieceColor())){//checks that they aren't equal
                    //ending piece is white
                    Move move = new Move(startRow, startCol, finishRow, finishCol, chessPieces[finishRow][finishCol]);
                    moves.add(move);
                    chessPieces[finishRow][finishCol].setPieceState(ChessPiece.PieceState.DEAD);
                    if(chessPieces[finishRow][finishCol].getPieceColor().equals(ChessPiece.PieceColor.WHITE)){
                        deadWhitePieces.add(chessPieces[finishRow][finishCol]);
                        aliveWhitePieces.remove(chessPieces[finishRow][finishCol]);
                        chessPieces[finishRow][finishCol] = chessPieces[startRow][startCol];
                        chessPieces[startRow][startCol] = null;
                        chessPieces[finishRow][finishCol].setPosition(finishRow, finishCol);
                        return true;
                    }else{//ending piece is black
                        deadBlackPieces.add(chessPieces[finishRow][finishCol]);
                        aliveBlackPieces.remove(chessPieces[finishRow][finishCol]);
                        chessPieces[finishRow][finishCol] = chessPieces[startRow][startCol];
                        chessPieces[startRow][startCol] = null;
                        chessPieces[finishRow][finishCol].setPosition(finishRow, finishCol);
                        return true;
                    }
                }
            }else{
                Move move = new Move(startRow, startCol, finishRow, finishCol, chessPieces[finishRow][finishCol]);
                moves.add(move);
                chessPieces[finishRow][finishCol] = chessPieces[startRow][startCol];
                chessPieces[startRow][startCol] = null;
                chessPieces[finishRow][finishCol].setPosition(finishRow, finishCol);
                return true;
            }
        }
        return false;
    }

    public void undoMove(){
        int lastMoveIndex = moves.size()-1;
        Move lastMove = moves.get(lastMoveIndex);
        System.out.println(lastMove);
        this.movePiece(lastMove.getFinishRow(),lastMove.getFinishCol(),lastMove.getStartRow(),lastMove.getStartCol(),false);  //returns piece to spot
        if(lastMove.getCapturedPiece()!=null) {
            if (lastMove.capturedPiece.getPieceColor().equals(ChessPiece.PieceColor.WHITE)) {//white piece
                deadWhitePieces.remove(lastMove.getCapturedPiece());
                aliveWhitePieces.add(lastMove.getCapturedPiece());
                chessPieces[lastMove.getCapturedPiece().getX()][lastMove.getCapturedPiece().getY()] = lastMove.getCapturedPiece();
                chessPieces[lastMove.getCapturedPiece().getX()][lastMove.getCapturedPiece().getY()].setPieceState(ChessPiece.PieceState.ALIVE);
            } else {//black piece
                deadBlackPieces.remove(lastMove.getCapturedPiece());
                aliveBlackPieces.add(lastMove.getCapturedPiece());
                chessPieces[lastMove.getCapturedPiece().getX()][lastMove.getCapturedPiece().getY()] = lastMove.getCapturedPiece();
                chessPieces[lastMove.getCapturedPiece().getX()][lastMove.getCapturedPiece().getY()].setPieceState(ChessPiece.PieceState.ALIVE);
            }
        }
    }


    public ChessPiece[][] generateInitalBoard(){
        ChessPiece[][] pieces = new ChessPiece[8][8];
        for (int i=0; i<pieces.length; i++) {//rows
            for (int j=0; j<pieces[i].length; j++) {//columns
                if(i==0||i==7){
                    switch (j){
                        case 0:
                        case 7:
                            Rook rook = new Rook();
                            if(i==0){
                                rook.setPieceColor(ChessPiece.PieceColor.BLACK);
                                rook.setPosition(i,j);
                                aliveBlackPieces.add(rook);
                            }else {
                                rook.setPieceColor(ChessPiece.PieceColor.WHITE);
                                rook.setPosition(i,j);
                                aliveWhitePieces.add(rook);
                            }
                            pieces[i][j] = rook;
                            break;
                        case 1:
                        case 6:
                            Knight knight = new Knight();
                            if(i==0){
                                knight.setPieceColor(ChessPiece.PieceColor.BLACK);
                                knight.setPosition(i,j);
                                aliveBlackPieces.add(knight);
                            }else {
                                knight.setPieceColor(ChessPiece.PieceColor.WHITE);
                                knight.setPosition(i,j);
                                aliveWhitePieces.add(knight);
                            }

                            pieces[i][j] = knight;

                            break;
                        case 2:
                        case 5:
                            Bishop bishop = new Bishop();
                            if(i==0){
                                bishop.setPieceColor(ChessPiece.PieceColor.BLACK);
                                bishop.setPosition(i,j);
                                aliveBlackPieces.add(bishop);
                            }else {
                                bishop.setPieceColor(ChessPiece.PieceColor.WHITE);
                                bishop.setPosition(i,j);
                                aliveWhitePieces.add(bishop);
                            }
                            pieces[i][j] = bishop;
                            break;
                        case 3:
                            Queen queen = new Queen();
                            if(i==0){
                                queen.setPieceColor(ChessPiece.PieceColor.BLACK);
                                queen.setPosition(i,j);
                                aliveBlackPieces.add(queen);
                            }else {
                                queen.setPieceColor(ChessPiece.PieceColor.WHITE);
                                queen.setPosition(i,j);
                                aliveWhitePieces.add(queen);
                            }
                            pieces[i][j] = queen;
                            break;
                        case 4:
                            if(i==0){
                                King king = new King();
                                king.setPieceColor(ChessPiece.PieceColor.BLACK);
                                king.setPosition(i,j);
                                pieces[i][j] = king;
                                blackKing = king;
                                aliveBlackPieces.add(king);
                            }else {
                                King king = new King();
                                king.setPieceColor(ChessPiece.PieceColor.WHITE);
                                king.setPosition(i,j);
                                pieces[i][j] = king;
                                whiteKing = king;
                                aliveWhitePieces.add(king);
                            }

                            break;
                    }

                }else if(i==1 || i==6){
                    Pawn pawn = new Pawn();
                    if(i==1){
                        pawn.setPieceColor(ChessPiece.PieceColor.BLACK);
                        pawn.setPieceState(ChessPiece.PieceState.ALIVE);
                        pawn.setPosition(i,j);
                        aliveBlackPieces.add(pawn);
                    }else {
                        pawn.setPieceColor(ChessPiece.PieceColor.WHITE);
                        pawn.setPieceState(ChessPiece.PieceState.ALIVE);
                        pawn.setPosition(i,j);
                        aliveWhitePieces.add(pawn);
                    }
                    pieces[i][j] = pawn;
                }
            }
        }
        return pieces;
    }

    public void outputBoard(){
        for (int i=0; i<chessPieces.length; i++) {//rows
            for (int j=0; j<chessPieces[i].length; j++) {//columns
                if(chessPieces[i][j]==null){
                    System.out.print(" - ");
                }else{
                    switch(chessPieces[i][j].getPieceType()){
                        case KING:
                            System.out.print(" K ");
                            break;
                        case PAWN:
                            System.out.print(" P ");
                            break;
                        case ROOK:
                            System.out.print(" R ");
                            break;
                        case QUEEN:
                            System.out.print(" Q ");
                            break;
                        case BISHOP:
                            System.out.print(" B ");
                            break;
                        case KNIGHT:
                            System.out.print(" k ");
                            break;
                    }
                }

            }
            System.out.println();
        }
    }

    //check for checkmate / check

}

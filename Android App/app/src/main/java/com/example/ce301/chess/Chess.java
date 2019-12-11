package com.example.ce301.chess;

import java.io.Serializable;
import java.util.ArrayList;

public class Chess implements Serializable {

    //Who is Black?
    //Who is White?
    //Display of grid is client side, Your colour should be closest to you
    //Server holds grid in one state, White at the bottom black at the top
    //So on one client black will be at the bottom, need a way of flipping the moves recieved from the player.
    //EG player moves piece [7][6] to [5][5] as a black player it should be converted to [0][1] to [2][2]




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

    public ChessPiece[][] chessPieces;
    public ArrayList<ChessPiece> deadWhitePieces;
    public ArrayList<ChessPiece> deadBlackPieces;

    public King blackKing;
    public King whiteKing;

    public boolean whosMoving;  //true = white
                                // false = black



    public Chess() {
        this.chessPieces = generateInitalBoard();
        this.deadWhitePieces = new ArrayList<>();
        this.deadBlackPieces = new ArrayList<>();
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
                            }else {
                                rook.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            rook.setPosition(i,j);
                            pieces[i][j] = rook;
                            break;
                        case 1:
                        case 6:
                            Knight knight = new Knight();
                            if(i==0){
                                knight.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                knight.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            knight.setPosition(i,j);
                            pieces[i][j] = knight;

                            break;
                        case 2:
                        case 5:
                            Bishop bishop = new Bishop();
                            if(i==0){
                                bishop.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                bishop.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            bishop.setPosition(i,j);
                            pieces[i][j] = bishop;
                            break;
                        case 3:
                            Queen queen = new Queen();
                            if(i==0){
                                queen.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                queen.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            queen.setPosition(i,j);
                            pieces[i][j] = queen;
                            break;
                        case 4:
                            King king = new King();
                            if(i==0){
                                king.setPieceColor(ChessPiece.PieceColor.BLACK);
                                blackKing = king;
                            }else {
                                king.setPieceColor(ChessPiece.PieceColor.WHITE);
                                whiteKing = king;
                            }
                            king.setPosition(i,j);
                            pieces[i][j] = king;
                            break;
                    }

                }else if(i==2 || i==5){
                    Pawn pawn = new Pawn();
                    if(i==2){
                        pawn.setPieceColor(ChessPiece.PieceColor.BLACK);
                    }else {
                        pawn.setPieceColor(ChessPiece.PieceColor.WHITE);
                    }
                    pawn.setPieceState(ChessPiece.PieceState.ALIVE);
                    pawn.setPosition(i,j);
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

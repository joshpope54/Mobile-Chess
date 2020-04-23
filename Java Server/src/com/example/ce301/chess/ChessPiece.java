package com.example.ce301.chess;

import java.io.Serializable;

public abstract class ChessPiece implements Serializable {
    protected ChessPiece() {
        pieceState = PieceState.ALIVE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public enum PieceType {
        PAWN,
        ROOK,
        KNIGHT,
        BISHOP,
        QUEEN,
        KING
    }

    public enum PieceColor {
        WHITE,
        BLACK
    }

    public enum PieceState{
        ALIVE,
        DEAD
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    private PieceType pieceType;
    private PieceColor pieceColor;
    private PieceState pieceState;
    private int x;
    private int y;

    public ChessPiece(PieceType pieceType, PieceColor pieceColor, PieceState pieceState) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.pieceState = pieceState;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public PieceState getPieceState() {
        return pieceState;
    }

    public void setPieceColor(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public void setPieceState(PieceState pieceState) {
        this.pieceState = pieceState;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    //Problem:
    //Determining which player is each color
    //Your own colour should always be closest to you in the app
    //? how to fix this in the arrays since if a player requests a move and it doesnt match up with the server then
    //it will move the color for that side on the server.
    //EG right now white will be closest to the player, What if the player was black? If they request a move as black
    //but the server thinks they did it as white, the white piece will move
    //Perhaps make a parser
    //EG player requests piece move A4 to A6;
    //Parse this so that a array is able to distinguish what piece was moved. <This shouldnt matter,
    //If we make the display only client side
    //{Fixed} - > use/th.abs to flip board -7
    //Create representations of boards client side

    public abstract Reason move(Chess chessboard, int row, int col, int type);

    @Override
    public String toString() {
        //return "{Type: "+getPieceType()+" Color:"+getPieceColor()+" State: "+getPieceState()+"}";
        return "Type: "+getPieceType()+" Color: "+getPieceColor() +" X:"+this.getX()+" Y:"+this.getY();
    }
}
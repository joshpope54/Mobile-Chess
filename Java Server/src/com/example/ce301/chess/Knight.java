package com.example.ce301.chess;

import java.io.Serializable;

public class Knight extends ChessPiece implements Serializable {
    public Knight() {
        setPieceType(PieceType.KNIGHT);
    }

    int[][] potentialMoves = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};

    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {

        boolean success = false;
        for(int[] possibleMove: potentialMoves){
            if(finishRow-getX()==possibleMove[0] && finishCol-getY()==possibleMove[1]){
                if(chess.chessPieces[finishRow][finishCol]==null){
                    if(type==0){
                        chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                    }
                    success = true;
                }else if (chess.chessPieces[finishRow][finishCol].getPieceColor() != this.getPieceColor()) {
                    if(type==0){
                        chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                    }
                    success = true;
                }else{
                    success=false;
                }
            }
        }
        if(success){
            return new Reason(true, "SUCCESS");
        }else{
            return new Reason(false, "FAILURE");
        }
    }
}

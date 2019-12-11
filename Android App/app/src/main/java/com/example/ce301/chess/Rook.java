package com.example.ce301.chess;

import java.io.Serializable;

public class Rook extends ChessPiece implements Serializable {


    public Rook() {
        setPieceType(PieceType.ROOK);
    }



    @Override
    public boolean move(Chess chessboard, int finishRow, int finishCol) {
        //look for pieces nearby
        //if final position x or y is greater than nearby position, move is invalid
        //determine which way the piece is moving
        //When do i take? a piece?
        boolean success = false;

        if(finishRow==getX()){
            //staying on same row
            if(finishCol>getY()){
                //moving right
                for(int i=getY()+1; i<=finishCol; i++){
                    if(chessboard.chessPieces[finishRow][i] == null){
                        if(i!=finishCol){
                            continue;
                        }else{
                            movePieceInArray(chessboard, finishRow, finishCol);
                            success=true;
                        }
                    }else{
                        if(i!=finishCol){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chessboard.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                movePieceInArray(chessboard, finishRow, finishCol);
                                success = true;
                            }
                        }
                    }
                }
            }else if(finishCol<getY()){
                //moving left
                for(int i=getY()-1; i>=finishCol; i--){
                    if(chessboard.chessPieces[finishRow][i] == null){
                        if(i!=finishCol){
                            continue;
                        }else{
                            movePieceInArray(chessboard, finishRow, finishCol);
                            success=true;
                        }
                    }else{
                        if(i!=finishCol){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chessboard.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                movePieceInArray(chessboard, finishRow, finishCol);
                                success = true;
                            }
                        }
                    }
                }
            }
        }else if(finishCol==getY()){
            //staying on same column
            if(finishRow>getX()){
                //moving forward
                for(int i=getX()+1; i<=finishRow; i++) {
                    if(chessboard.chessPieces[i][finishCol] == null){
                        if(i!=finishRow){
                            continue;
                        }else{
                            movePieceInArray(chessboard, finishRow, finishCol);
                            success=true;
                        }
                    }else{
                        if(i!=finishRow){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chessboard.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                movePieceInArray(chessboard, finishRow, finishCol);
                                success = true;
                            }
                        }
                    }
                }
            }else if(finishRow<getX()) {
                //moving moving backward
                for(int i=getX()-1; i>=finishRow; i--){
                    if(chessboard.chessPieces[i][finishCol] == null){
                        if(i!=finishRow){
                            continue;
                        }else{
                            movePieceInArray(chessboard, finishRow, finishCol);
                            success=true;
                        }
                    }else{
                        if(i!=finishRow){
                            break;
                        }else{
                            if(this.getPieceColor().equals(chessboard.chessPieces[finishRow][finishCol].getPieceColor())){
                                success=false;
                            }else{
                                movePieceInArray(chessboard, finishRow, finishCol);
                                success = true;
                            }
                        }
                    }
                }
            }
        }

        if(success){
            return true;
        }else {
            return false;
        }
    }
}

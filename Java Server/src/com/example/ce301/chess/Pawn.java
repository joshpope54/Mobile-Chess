package com.example.ce301.chess;

import java.io.Serializable;

public class Pawn extends ChessPiece implements Serializable {



    public Pawn() {
        setPieceType(PieceType.PAWN);
    }

    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {
        //get chess board instance
        //System.out.println(chess.chessPieces[this.getX()][this.getY()]);
        //check if piece is able to be moved to position
        //pawn
        //1 forward
        //2 forward if in first position
        //diagonal if can take piece
        //Question ->
        //How to check diagonal?
        //perhaps valid moves array?
        //First check if in default position to allow movement of 2 forward
        //check if moving backwards
        //cant allow that

        //en passant
        //if on 5th rank
        //and last move was a pawn 2 forward
        //then can enpassant


        if(getPieceColor().equals(PieceColor.WHITE)){
            if(getX()-finishRow==-1){
                return new Reason(false, "FAILURE");
            }else{
                Boolean x = getaBoolean(chess, finishRow, finishCol, type);
                return new Reason(x, "SUCCESS");

            }
        }else{
            if(finishRow-getX()==-1){
                return new Reason(false, "FAILURE");
            }else{
                Boolean x = getaBoolean(chess, finishRow, finishCol, type);
                return new Reason(x, "SUCCESS");
            }
        }
    }

    private Boolean getaBoolean(Chess chess, int finishRow, int finishCol, int type) {
        if(getX()+1==finishRow && getY()+1==finishCol){ //x+1 y+1, Black down and right  //need to add left down for black
            return completeMove(chess, finishRow, finishCol, type);
        }else if(getX()-1==finishRow && getY()-1==finishCol) { //x+1 y+1, Black down and right  //need to add left down for black
            return completeMove(chess, finishRow, finishCol, type);
        }else if(getX()+1==finishRow && getY()-1==finishCol) { //x+1 y+1, Black down and right  //need to add left down for black
            return completeMove(chess, finishRow, finishCol, type);
        }else if(getX()-1==finishRow && getY()+1==finishCol) { //x+1 y+1, Black down and right  //need to add left down for black
            return completeMove(chess, finishRow, finishCol, type);
        }else{
            if(finishCol!=getY()){
                return false;
            }else{
                if(getX()==1 || getX()==6 ){
                    if(getPieceColor().equals(PieceColor.WHITE)) {
                        if (getX() - finishRow == 2 || getX() - finishRow == 1) { //WHITE GOING FOWARD
                            if (chess.chessPieces[finishRow][finishCol] == null) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }else{
                        if (finishRow - getX() == 2 || finishRow - getX() == 1) {  //BLACK GOING FORWARD
                            if (chess.chessPieces[finishRow][finishCol] == null) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }else{
                    if(getPieceColor().equals(PieceColor.WHITE)) {
                        if (getX() - finishRow == 1) {
                            if (chess.chessPieces[finishRow][finishCol] == null) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }else{
                        if (finishRow - getX() == 1) {
                            if (chess.chessPieces[finishRow][finishCol] == null) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private Boolean completeMove(Chess chess, int finishRow, int finishCol, int type) {
        if (chess.chessPieces[finishRow][finishCol] != null) {
            if (!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                if(type==0){
                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                }
                return true;
            } else {
                return false;
            }
        } else {
            //check that its in the right spot
            Chess.Move lastMove = chess.moves.get(chess.moves.size()-1);
            ChessPiece pieceThatPerformedLastMove = chess.chessPieces[lastMove.getFinishRow()][lastMove.getFinishCol()];
            if(this.getPieceColor().equals(PieceColor.WHITE)){
                //white so correct row should be 3
                if(this.getX()==3){
                    //correct
                    if(pieceThatPerformedLastMove instanceof Pawn){
                        //white pawn is in correct spot and last piece that enemy moved was a pawn so now check its next to our pawn
                        if(this.getY()+1 == pieceThatPerformedLastMove.getY()){
                            //on the right
                            //check that the move places our pawn 1 behind the enemy pawn
                            if(finishRow == pieceThatPerformedLastMove.getX()-1){
                                if(finishCol == pieceThatPerformedLastMove.getY()){
                                    //its a en passant
                                    if(type == 0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol, true);
                                    }
                                    return true;
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else if (this.getY()-1 == pieceThatPerformedLastMove.getY()){
                            //on the left
                            if(finishRow == pieceThatPerformedLastMove.getX()-1){
                                if(finishCol == pieceThatPerformedLastMove.getY()){
                                    //its a en passant
                                    if(type == 0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol, true);
                                    }
                                    return true;
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else{
                            //invalid
                            return false;
                        }

                    }
                }else{
                    return false;
                }
            }else if (this.getPieceColor().equals(PieceColor.BLACK)){
                //black so correct row should be 4
                if(this.getX()==4){
                    //correct
                    if(pieceThatPerformedLastMove instanceof Pawn){
                        //white pawn is in correct spot and last piece that enemy moved was a pawn so now check its next to our pawn
                        if(this.getY()+1 == pieceThatPerformedLastMove.getY()){
                            //on the right
                            //check that the move places our pawn 1 behind the enemy pawn
                            if(finishRow == pieceThatPerformedLastMove.getX()+1){
                                if(finishCol == pieceThatPerformedLastMove.getY()){
                                    //its a en passant
                                    if(type == 0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol, true);
                                    }
                                    return true;
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else if (this.getY()-1 == pieceThatPerformedLastMove.getY()){
                            //on the left
                            if(finishRow == pieceThatPerformedLastMove.getX()+1){
                                if(finishCol == pieceThatPerformedLastMove.getY()){
                                    //its a en passant
                                    if(type == 0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol, true);
                                    }
                                    return true;
                                }else{
                                    return false;
                                }
                            }else{
                                return false;
                            }
                        }else{
                            //invalid
                            return false;
                        }

                    }
                }else{
                    return false;
                }
            }



            if(pieceThatPerformedLastMove instanceof Pawn){
                //its a pawn
                //check that it moved 2 forward and that its next to the pawn either side
            }

            return false;
        }
    }


}

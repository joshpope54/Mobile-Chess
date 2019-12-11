package com.example.ce301.chess;

import java.io.Serializable;

public class King extends ChessPiece implements Serializable{

    public King() {
        setPieceType(PieceType.KING);
    }

    private int[][] potentialPositions = {{-1,-1},{-1,+1},{+1,-1},{+1,+1},{+1,0},{-1,0},{0,+1},{0,-1}};

    @Override
    public boolean move(Chess chess, int finishRow, int finishCol) {
        //check that finish row or finish col is 1 away
        //check if null
        boolean success = false;
        for(int[] possibleMove: potentialPositions){
            if(finishRow-getX()==possibleMove[0] && finishCol-getY()==possibleMove[1]){
                if(chess.chessPieces[finishRow][finishCol]==null){
                    //check if in check
                    //king is most complex piece
                    //must first check if king is in check
                    //move must then remove king from check
                    movePieceInArray(chess, finishRow, finishCol);
                    success=true;
                }else{
                    //check if king is in check
                    if(!chess.chessPieces[finishRow][finishCol].getPieceColor().equals(this.getPieceColor())){
                        movePieceInArray(chess, finishRow, finishCol);
                        success = true;
                    }else{
                        success = false;
                    }
                }
            }
        }
        if(success){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkIfInCheck(Chess chess){
        //Check for any pieces attacking king
        //Check if king can move its self out of position
        boolean diag = ifTraceOnDiagonal(chess);
        boolean onX = ifTraceOnX(chess);
        boolean onY = ifTraceOnY(chess);
        return diag || onX || onY;
    }

    private String checkPieces(Chess chess, int i, int j){
        if(chess.chessPieces[i][j] != null){
            if(chess.chessPieces[i][j].getPieceColor().equals(this.getPieceColor())){
                return "OWNPIECE";
            }else{
                if(chess.chessPieces[i][j] instanceof Queen){
                    return "ENEMYPIECE";
                }else if(chess.chessPieces[i][j] instanceof Bishop){
                    return "ENEMYPIECE";
                }
            }
        }
        return "";
    }

    private boolean ifTraceOnDiagonal(Chess chess){
        String successFrom1 = "";
        String successFrom2 = "";
        String successFrom3 = "";
        String successFrom4 = "";

        boolean successBoolean1 = false;
        boolean successBoolean2 = false;
        boolean successBoolean3 = false;
        boolean successBoolean4 = false;


        for(int i=getX()+1, j=getY()+1; i<=7; i++, j++) {
            if(i==7 || j==7){
                successFrom1 = checkPieces(chess, i, j);
                if(successFrom1.equals("OWNPIECE")){
                    successBoolean1=false;
                    break;
                }else if (successFrom1.equals("ENEMYPIECE")){
                    successBoolean1=true;
                    break;
                }
                break;
            }else{
                successFrom1 = checkPieces(chess, i, j);
                if(successFrom1.equals("OWNPIECE")){
                    successBoolean1=false;
                    break;
                }else if (successFrom1.equals("ENEMYPIECE")){
                    successBoolean1=true;
                    break;
                }
            }
        }

        for(int i=getX()-1, j=getY()-1; i>=0; i--, j--) {
            if(i==0 || j==0){
                successFrom2 = checkPieces(chess, i, j);
                if(successFrom2.equals("OWNPIECE")){
                    successBoolean2=false;
                    break;
                }else if (successFrom2.equals("ENEMYPIECE")){
                    successBoolean2=true;
                    break;
                }
                break;
            }else{
                successFrom2 = checkPieces(chess, i, j);
                if(successFrom2.equals("OWNPIECE")){
                    successBoolean2=false;
                    break;
                }else if (successFrom2.equals("ENEMYPIECE")){
                    successBoolean2=true;
                    break;
                }
            }
        }

        for(int i=getX()-1, j=getY()+1; i>=0; i--, j++) {
            if(i==0 || j==7){
                successFrom3 = checkPieces(chess, i, j);
                if(successFrom3.equals("OWNPIECE")){
                    successBoolean3=false;
                    break;
                }else if (successFrom3.equals("ENEMYPIECE")){
                    successBoolean3=true;
                    break;
                }
                break;
            }else{
                successFrom3 = checkPieces(chess, i, j);
                if(successFrom3.equals("OWNPIECE")){
                    successBoolean3=false;
                    break;
                }else if (successFrom3.equals("ENEMYPIECE")){
                    successBoolean3=true;
                    break;
                }
            }
        }

        for(int i=getX()+1, j=getY()-1; i<=7; i++, j--) {
            if(i==7 || j==0){
                successFrom4 = checkPieces(chess, i, j);
                if(successFrom4.equals("OWNPIECE")){
                    successBoolean4=false;
                    break;
                }else if (successFrom4.equals("ENEMYPIECE")){
                    successBoolean4=true;
                    break;
                }
                break;
            }else{
                successFrom4 = checkPieces(chess, i, j);
                if(successFrom4.equals("OWNPIECE")){
                    successBoolean4=false;
                    break;
                }else if (successFrom4.equals("ENEMYPIECE")){
                    successBoolean4=true;
                    break;
                }
            }
        }
        return successBoolean1 || successBoolean2 || successBoolean3 || successBoolean4;
    }

    @SuppressWarnings("Duplicates")
    public boolean ifTraceOnY(Chess chess){
        boolean success = false;
        for(int i=getX()-1; i>=0;i--){
            if(chess.chessPieces[i][getY()] != null){
                if(chess.chessPieces[i][getY()].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[i][getY()] instanceof Queen){
                        success = true;
                        break;
                    }else if(chess.chessPieces[i][getY()] instanceof Rook){
                        success = true;
                        break;
                    }
                }
            }
        }
        for(int i=getX()+1; i<=7;i++){
            if(chess.chessPieces[i][getY()] != null){
                if(chess.chessPieces[i][getY()].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[i][getY()] instanceof Queen){
                        success = true;
                        break;
                    }else if(chess.chessPieces[i][getY()] instanceof Rook){
                        success = true;
                        break;
                    }
                }
            }
        }
        return success;
    }


    @SuppressWarnings("Duplicates")
    private boolean ifTraceOnX(Chess chess){
        boolean success = false;
        for(int i=getY()-1; i>=0;i--){
            if(chess.chessPieces[getX()][i] != null){
                if(chess.chessPieces[getX()][i].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[getX()][i] instanceof Queen){
                        success = true;
                        break;
                    }else if(chess.chessPieces[getX()][i] instanceof Rook){
                        success = true;
                        break;
                    }
                }
            }
        }
        for(int i=getY()+1; i<=7;i++){
            if(chess.chessPieces[getX()][i] != null){
                if(chess.chessPieces[getX()][i].getPieceColor().equals(this.getPieceColor())){
                    success = false;
                    break;
                }else{
                    if(chess.chessPieces[getX()][i] instanceof Queen){
                        success = true;
                        break;
                    }else if(chess.chessPieces[getX()][i] instanceof Rook){
                        success = true;
                        break;

                    }
                }
            }
        }
        return success;
    }
}

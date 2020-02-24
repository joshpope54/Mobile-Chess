package com.example.ce301.chess;

import sun.awt.windows.ThemeReader;

import java.io.Serializable;

public class King extends ChessPiece implements Serializable{

    public King() {
        setPieceType(PieceType.KING);
    }

    private int[][] potentialPositions = {{-1,-1},{-1,+1},{+1,-1},{+1,+1},{+1,0},{-1,0},{0,+1},{0,-1}};

    @Override
    public Reason move(Chess chess, int finishRow, int finishCol) {
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
                    int startX = getX();
                    int startY = getY();
                    movePieceInArray(chess, finishRow, finishCol);
                    if(this.checkIfInCheck(chess)){
                        movePieceInArray(chess, startX, startY);
                        success = false;
                    }else{
                        success=true;
                    }
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
            return new Reason(true, "SUCCESS");
        }else{
            return new Reason(false, "FAILURE");
        }
    }

    public boolean checkIfInCheck(Chess chess){
        //Check for any pieces attacking king
        //Check if king can move its self out of position
        //check for knight
        boolean knightCheck = ifTraceOnKnights(chess);
        System.out.println("knight check " + knightCheck);
        boolean diag = ifTraceOnDiagonal(chess);
        //System.out.println(diag);
        boolean onX = ifTraceOnX(chess);
        //System.out.println(onX);
        boolean onY = ifTraceOnY(chess);
        //System.out.println(onY);
        return diag || onX || onY || knightCheck;
    }

    private String checkPieces(Chess chess, int i, int j){
        if(chess.chessPieces[i][j] != null){
            if(chess.chessPieces[i][j].getPieceColor().equals(this.getPieceColor())){
                return "OWNPIECE ";
            }else{
                if(chess.chessPieces[i][j] instanceof Queen){
                    return "ENEMYPIECE QUEEN";
                }else if(chess.chessPieces[i][j] instanceof Bishop){
                    return "ENEMYPIECE BISHOP";
                }else if(chess.chessPieces[i][j] instanceof Knight){
                    return "ENEMYPIECE KNIGHT";
                }else if(chess.chessPieces[i][j] instanceof Pawn){
                    return "ENEMYPIECE PAWN";
                }
            }
        }
        return "";
    }

    private boolean ifTraceOnKnights(Chess chess){
        int[][] potentialMoves = {{2,1},{1,2},{-2,1},{-1,2},{-2,-1},{-1,-2},{2,-1},{1,-2}};

        for (int[] move : potentialMoves){
            try {
                if(chess.chessPieces[getX()+move[0]][getY()+move[1]]!=null){
                    if(!chess.chessPieces[getX()+move[0]][getY()+move[1]].getPieceColor().equals(this.getPieceColor())){
                        if(chess.chessPieces[getX()+move[0]][getY()+move[1]] instanceof Knight){
                            return true;
                        }
                    }
                }
            }catch (ArrayIndexOutOfBoundsException ignored){
            }
        }
        return false;
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
            successFrom1 = checkPieces(chess, i, j);
            String[] successArray = successFrom1.split(" ");
            if(i==7 || j==7){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
            }
        }

        for(int i=getX()-1, j=getY()-1; i>=0; i--, j--) {
            successFrom2 = checkPieces(chess, i, j);
            String[] successArray = successFrom2.split(" ");
            if(i==0 || j==0){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
            }
        }

        for(int i=getX()-1, j=getY()+1; i>=0; i--, j++) {
            successFrom3 = checkPieces(chess, i, j);
            String[] successArray = successFrom3.split(" ");
            if(i==0 || j==7){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }
                    }
                    break;
                }
            }
        }

        for(int i=getX()+1, j=getY()-1; i<=7; i++, j--) {
            successFrom4 = checkPieces(chess, i, j);
            String[] successArray = successFrom4.split(" ");

            if(i==7 || j==0){
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }                    }
                    break;
                }
                break;
            }else{
                if(successArray[0].equals("OWNPIECE")){
                    break;
                }else if (successArray[0].equals("ENEMYPIECE")){
                    if(!successArray[1].equals("KNIGHT")){
                        if(successArray[1].equals("PAWN")){
                            //check distance from king
                            //i,j found piece
                            if(Math.abs(i-getX())==1 && Math.abs(j-getY())==1 ){
                                successBoolean1=true;
                            }else{
                                successBoolean1=false;
                            }
                        }else{
                            successBoolean1=true;
                        }                    }
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

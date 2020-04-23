package com.example.ce301.chess;

import java.io.Serializable;

public class Bishop extends ChessPiece implements Serializable {
    public Bishop() {
        setPieceType(PieceType.BISHOP);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {
        boolean success = false;
        // want to move diagonal
        if(getX()==finishRow || getY()==finishCol){
            return new Reason(false, "FAILURE");
        }else{
            //Only works for down and right
            if(finishRow-getX()>0 && finishCol-getY()>0){
                for(int i=getX()+1, j=getY()+1; i<=finishRow && j <=finishCol; i++, j++){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                            }
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                success= true;
                            }
                        }else{
                            return new Reason(false, "FAILURE");
                        }
                    }
                }
            }else if (finishRow-getX()<0 && finishCol-getY()<0){
                for(int i=getX()-1, j=getY()-1; i>=finishRow && j>=finishCol; i--, j--){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                            }
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                success= true;
                            }
                        }else{
                            return new Reason(false, "FAILURE");
                        }
                    }
                }
            }else if (finishRow-getX()<0 && finishCol-getY()>0){
                for(int i=getX()-1, j=getY()+1; i>=finishRow && j<=finishCol; i--, j++){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                            }
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                success= true;
                            }
                        }else{
                            return new Reason(false, "FAILURE");
                        }
                    }
                }
            }else if (finishRow-getX()>0 && finishCol-getY()<0){
                for(int i=getX()+1, j=getY()-1; i<=finishRow && j>=finishCol; i++, j--){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                            }
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol,false);
                                }
                                success= true;
                            }
                        }else{
                            return new Reason(false, "FAILURE");
                        }
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
}

package chess;

public class Bishop extends ChessPiece {
    public Bishop() {
        setPieceType(PieceType.BISHOP);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean move(Chess chess, int finishRow, int finishCol) {
        boolean success = false;
        // want to move diagonal
        if(getX()==finishRow || getY()==finishCol){
            return false;
        }else{
            //Only works for down and right
            if(finishRow-getX()>0 && finishCol-getY()>0){
                for(int i=getX()+1, j=getY()+1; i<=finishRow; i++, j++){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            movePieceInArray(chess, finishRow, finishCol);
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                movePieceInArray(chess, finishRow, finishCol);
                                success= true;
                            }
                        }else{
                            return false;
                        }
                    }
                }
            }else if (finishRow-getX()<0 && finishCol-getY()<0){
                for(int i=getX()-1, j=getY()-1; i>=finishRow; i--, j--){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            movePieceInArray(chess, finishRow, finishCol);
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                movePieceInArray(chess, finishRow, finishCol);
                                success= true;
                            }
                        }else{
                            return false;
                        }
                    }
                }
            }else if (finishRow-getX()<0 && finishCol-getY()>0){
                for(int i=getX()-1, j=getY()+1; i>=finishRow; i--, j++){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            movePieceInArray(chess, finishRow, finishCol);
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                movePieceInArray(chess, finishRow, finishCol);
                                success= true;
                            }
                        }else{
                            return false;
                        }
                    }
                }
            }else if (finishRow-getX()>0 && finishCol-getY()<0){
                for(int i=getX()+1, j=getY()-1; i<=finishRow; i++, j--){
                    if(chess.chessPieces[i][j] == null){
                        if(i==finishRow && j==finishCol){
                            movePieceInArray(chess, finishRow, finishCol);
                            success= true;
                        }
                    }else{
                        if(i==finishRow && j==finishCol){
                            if(!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())){
                                movePieceInArray(chess, finishRow, finishCol);
                                success= true;
                            }
                        }else{
                            return false;
                        }
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
}

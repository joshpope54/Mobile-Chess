package com.example.ce301.chess;

import java.io.Serializable;

public class Queen extends ChessPiece implements Serializable {
    public Queen() {
        setPieceType(PieceType.QUEEN);
    }

    @Override
    public Reason move(Chess chess, int finishRow, int finishCol, int type) {
        //queen
        //rook and bishop combined
        boolean success = false;
        if(getX()==finishRow || getY()==finishCol) {
            if (finishRow == getX()) {
                //staying on same row
                if (finishCol > getY()) {
                    //moving right
                    for (int i = getY() + 1; i <= finishCol; i++) {
                        if (chess.chessPieces[finishRow][i] == null) {
                            if (i != finishCol) {
                                continue;
                            } else {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            if (i != finishCol) {
                                break;
                            } else {
                                if (this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                    success = false;
                                } else {
                                    if(type==0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol);
                                    }
                                    success = true;
                                }
                            }
                        }
                    }
                } else if (finishCol < getY()) {
                    //moving left
                    for (int i = getY() - 1; i >= finishCol; i--) {
                        if (chess.chessPieces[finishRow][i] == null) {
                            if (i != finishCol) {
                                continue;
                            } else {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            if (i != finishCol) {
                                break;
                            } else {
                                if (this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                    success = false;
                                } else {
                                    if(type==0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol);
                                    }
                                    success = true;
                                }
                            }
                        }
                    }
                }
            } else if (finishCol == getY()) {
                //staying on same column
                if (finishRow > getX()) {
                    //moving forward
                    for (int i = getX() + 1; i <= finishRow; i++) {
                        if (chess.chessPieces[i][finishCol] == null) {
                            if (i != finishRow) {
                                continue;
                            } else {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            if (i != finishRow) {
                                break;
                            } else {
                                if (this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                    success = false;
                                } else {
                                    if(type==0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol);
                                    }
                                    success = true;
                                }
                            }
                        }
                    }
                } else if (finishRow < getX()) {
                    //moving moving backward
                    for (int i = getX() - 1; i >= finishRow; i--) {
                        if (chess.chessPieces[i][finishCol] == null) {
                            if (i != finishRow) {
                                continue;
                            } else {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            if (i != finishRow) {
                                break;
                            } else {
                                if (this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                    success = false;
                                } else {
                                    if(type==0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol);
                                    }
                                    success = true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            //Only works for down and right
            if (finishRow - getX() > 0 && finishCol - getY() > 0) {
                for (int i = getX() + 1, j = getY() + 1; i <= finishRow && j <=finishCol; i++, j++) {
                    if (chess.chessPieces[i][j] == null) {
                        if (i == finishRow && j == finishCol) {
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success = true;
                        }
                    } else {
                        if (i == finishRow && j == finishCol) {
                            if (!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            return new Reason(false, "FAILURE");
                        }
                    }
                }
            } else if (finishRow - getX() < 0 && finishCol - getY() < 0) {
                for (int i = getX() - 1, j = getY() - 1; i >= finishRow && j>=finishCol; i--, j--) {
                    if (chess.chessPieces[i][j] == null) {
                        if (i == finishRow && j == finishCol) {
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success = true;
                        }
                    } else {
                        if (i == finishRow && j == finishCol) {
                            if (!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            return new Reason(false, "FAILURE");
                        }
                    }
                }
            } else if (finishRow - getX() < 0 && finishCol - getY() > 0) {
                for (int i = getX() - 1, j = getY() + 1; i >= finishRow && j<=finishCol; i--, j++) {
                    if(i<8 && j<8){
                        if (chess.chessPieces[i][j] == null) {
                            if (i == finishRow && j == finishCol) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
                            if (i == finishRow && j == finishCol) {
                                if (!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                    if(type==0){
                                        chess.movePiece(getX(), getY(), finishRow, finishCol);
                                    }
                                    success = true;
                                }
                            } else {
                                return new Reason(false, "FAILURE");
                            }
                        }
                    }
                }
            } else if (finishRow - getX() > 0 && finishCol - getY() < 0) {
                for (int i = getX() + 1, j = getY() - 1; i <= finishRow && j>=finishCol; i++, j--) {
                    if (chess.chessPieces[i][j] == null) {
                        if (i == finishRow && j == finishCol) {
                            if(type==0){
                                chess.movePiece(getX(), getY(), finishRow, finishCol);
                            }
                            success = true;
                        }
                    } else {
                        if (i == finishRow && j == finishCol) {
                            if (!this.getPieceColor().equals(chess.chessPieces[finishRow][finishCol].getPieceColor())) {
                                if(type==0){
                                    chess.movePiece(getX(), getY(), finishRow, finishCol);
                                }
                                success = true;
                            }
                        } else {
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

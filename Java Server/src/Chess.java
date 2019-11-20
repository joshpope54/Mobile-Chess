public class Chess {


    private ChessPieces[][] chessPieces;

    public Chess() {
        this.chessPieces = generateInitalBoard();
    }

    public ChessPieces[][] generateInitalBoard(){
        ChessPieces[][] pieces = new ChessPieces[8][8];

        //Generate items!
        //i row
        //j column

        //if i == 0 || i == 7 backrow
        //if i == 1 || i == 6 pawns

        for (int i=0; i<pieces.length; i++) {//rows
            for (int j=0; j<pieces[i].length; j++) {//columns
                if(i==0||i==7){
                    switch (j){
                        case 0:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.ROOK, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 7:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.ROOK, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 1:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.KNIGHT, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 6:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.KNIGHT, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 2:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.BISHOP, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 5:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.BISHOP, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 3:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.QUEEN, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                        case 4:
                            pieces[i][j] = new ChessPieces(ChessPieces.PieceType.KING, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                            break;
                    }

                }else if(i==1 || i==6){
                    pieces[i][j] = new ChessPieces(ChessPieces.PieceType.PAWN, ChessPieces.PieceColor.WHITE, ChessPieces.PieceState.ALIVE);
                }
                System.out.print(pieces[i][j]);
            }
            System.out.println();
        }
        return pieces;
    }

}

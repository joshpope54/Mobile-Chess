package chess;

public class Chess {

    //Who is Black?
    //Who is White?
    //Display of grid is client side, Your colour should be closest to you
    //Server holds grid in one state, White at the bottom black at the top
    //So on one client black will be at the bottom, need a way of flipping the moves recieved from the player.
    //EG player moves piece [7][6] to [5][5] as a black player it should be converted to [0][1] to [2][2]

    private ChessPiece[][] chessPieces;

    public Chess() {
        this.chessPieces = generateInitalBoard();
    }

    public ChessPiece[][] generateInitalBoard(){
        ChessPiece[][] pieces = new ChessPiece[8][8];
        for (int i=0; i<pieces.length; i++) {//rows
            for (int j=0; j<pieces[i].length; j++) {//columns
                if(i==0||i==7){
                    switch (j){
                        case 0:
                        case 7:
                            Rook rook = new Rook();
                            if(i==0){
                                rook.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                rook.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            pieces[i][j] = rook;

                            break;
                        case 1:
                        case 6:
                            Knight knight = new Knight();
                            if(i==0){
                                knight.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                knight.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            pieces[i][j] = knight;
                            break;
                        case 2:
                        case 5:
                            Bishop bishop = new Bishop();
                            if(i==0){
                                bishop.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                bishop.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            pieces[i][j] = bishop;
                            break;
                        case 3:
                            Queen queen = new Queen();
                            if(i==0){
                                queen.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                queen.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            pieces[i][j] = queen;
                            break;
                        case 4:
                            King king = new King();
                            if(i==0){
                                king.setPieceColor(ChessPiece.PieceColor.BLACK);
                            }else {
                                king.setPieceColor(ChessPiece.PieceColor.WHITE);
                            }
                            pieces[i][j] = king;
                            break;
                    }

                }else if(i==1 || i==6){
                    //pieces[i][j]
                    Pawn pawn = new Pawn();
                    if(i==1){
                        pawn.setPieceColor(ChessPiece.PieceColor.BLACK);
                    }else {
                        pawn.setPieceColor(ChessPiece.PieceColor.WHITE);
                    }
                    pawn.setPieceState(ChessPiece.PieceState.ALIVE);
                    pieces[i][j] = pawn;

                }
                System.out.print(pieces[i][j]);
            }
            System.out.println();
        }
        return pieces;
    }

}

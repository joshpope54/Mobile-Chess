package chess;

public class King extends ChessPiece{

    public King() {
        setPieceType(PieceType.KING);
    }

    @Override
    public boolean move(Chess chessboard, int row, int col) {
        return false;
    }

    //how can i check for check
    //or check mate

}

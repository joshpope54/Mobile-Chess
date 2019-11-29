package chess;

public class King extends ChessPiece{

    public King() {
        setPieceType(PieceType.KING);
    }

    @Override
    public boolean move() {
        return false;
    }
}

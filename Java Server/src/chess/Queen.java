package chess;

public class Queen extends ChessPiece {
    public Queen() {
        setPieceType(PieceType.QUEEN);
    }

    @Override
    public boolean move(Chess chessboard, int row, int col) {
        return false;
    }
}

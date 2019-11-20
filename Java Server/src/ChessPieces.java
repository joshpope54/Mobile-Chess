public class ChessPieces {
    public enum PieceType {
        PAWN,
        ROOK,
        KNIGHT,
        BISHOP,
        QUEEN,
        KING
    }

    public enum PieceColor {
        WHITE,
        BLACK
    }

    public enum PieceState{
        ALIVE,
        DEAD
    }

    private PieceType pieceType;
    private PieceColor pieceColor;
    private PieceState pieceState;

    public ChessPieces(PieceType pieceType, PieceColor pieceColor, PieceState pieceState) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.pieceState = pieceState;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public PieceState getPieceState() {
        return pieceState;
    }

    @Override
    public String toString() {
        return "{Type: "+getPieceType()+" Color:"+getPieceColor()+" State: "+getPieceState()+"}";
    }
}
package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        //return piece color
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //how pawns move
        if(this.pieceType == PieceType.PAWN){
            System.out.println("Hello, PAWN!");
            /*1. get the row and column of where the piece is on the board
            *2. establish rules for the pawn class only allowing it to go up once and
            * only diagonal once if there is a piece there  */
        }

        //how king moves
        else if(this.pieceType == PieceType.KING) {
            System.out.println("Hello, KING!");
        }
        //how knight moves
        else if(this.pieceType == PieceType.KNIGHT) {
            System.out.println("Hello, KNIGHT!");
        }
        //how bishop moves
        else if(this.pieceType == PieceType.BISHOP) {
            System.out.println("Hello, BISHOP!");
        }

        //how rook moves
        else if(this.pieceType == PieceType.ROOK){
            System.out.println("Hello, ROOK!");
        }

        //how queen moves
        else if(this.pieceType == PieceType.QUEEN){
            System.out.println("Hello, QUEEN!");
        }
        return new ArrayList<>();
    }
}

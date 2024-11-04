package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

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


    @Override
    public boolean equals(Object o) {
        //if my address matches default equals
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        //make a new object called THAT; CASTING
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public void howBlackPawnMoves(ChessBoard board, Collection <ChessMove> listMoves,
                                  ChessPosition myPosition, int newRow, int newCol){
        for (int i = 0; i < 3; i++) {
            if (newRow < 1 || newCol < 1 || newCol > 8) {
                continue;
            }
            //new chess piece position it can maybe go to
            if (i == 0 || i ==2) {
                int baseCol = newCol + (i-1);
                ChessPosition leftDownPosition = new ChessPosition(newRow-1,
                        baseCol);
                if (newRow < 1 || baseCol < 1 || baseCol > 8) {
                    continue;
                }
                ChessMove basecaseMove = new ChessMove(myPosition, leftDownPosition, null);
                if (board.getPiece(leftDownPosition) == null ||
                        (board.getPiece(leftDownPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                    continue;
                }
                else if (board.getPiece(leftDownPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if(pawnWhitePromotionCheck(leftDownPosition.getRow()) == true){
                        createAllPromotionalPieces(myPosition,leftDownPosition,listMoves);
                        continue;
                    }
                    listMoves.add(basecaseMove);
                    continue;
                }
            }
            ChessPosition pnewPosition = new ChessPosition(newRow - 1, newCol + (i - 1));
            //new move
            ChessMove moves = new ChessMove(myPosition, pnewPosition, null);
            if (board.getPiece(pnewPosition) == null) {
                if(pawnWhitePromotionCheck(pnewPosition.getRow()) == true){
                    createAllPromotionalPieces(myPosition,pnewPosition,listMoves);
                    continue;
                }
                listMoves.add(moves);
            }
            else if (board.getPiece(pnewPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                //if pawn is trying to go vertical color doesn't matter, skip
                if (i==1) {
                    continue;
                }
                listMoves.add(moves);
            }
        }
    }
    public void howPawnMoves(ChessBoard board, Collection<ChessMove> listMoves, ChessPosition myPosition,
                             int newRow, int newCol){
        for (int i = 0; i < 3; i++) {
            if (newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            //new chess piece position it can maybe go to
            if (i == 0 || i == 2) {
                int baseCol = newCol + (i-1);
                ChessPosition leftUpPosition = new ChessPosition(newRow + 1,
                        baseCol);
                if (newRow > 8 || baseCol < 1 || baseCol > 8) {
                    continue;
                }
                ChessMove basecaseMove = new ChessMove(myPosition, leftUpPosition, null);
                if (board.getPiece(leftUpPosition) == null ||
                        (board.getPiece(leftUpPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor())) {
                    continue;
                } else if (board.getPiece(leftUpPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if(pawnWhitePromotionCheck(leftUpPosition.getRow()) == true){
                        createAllPromotionalPieces(myPosition,leftUpPosition,listMoves);
                        continue;
                    }
                    listMoves.add(basecaseMove);
                    continue;
                }
            }
            ChessPosition newPosition = new ChessPosition(newRow + 1, newCol + (i - 1));
            //new move
            ChessMove moves = new ChessMove(myPosition, newPosition, null);
            if (board.getPiece(newPosition) == null) {
                if(pawnWhitePromotionCheck(newPosition.getRow()) == true){
                    createAllPromotionalPieces(myPosition,newPosition,listMoves);
                    continue;
                }
                listMoves.add(moves);
            }
            else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                if (i==1) {
                    continue;
                }
                listMoves.add(moves);

            }

        }
    }

    public void howRookMoves(ChessPosition myPosition, ChessBoard board,
                             Collection<ChessMove> listMoves) {
        // Define offsets for each direction: left, right, up, down
        int[][] directions = {
                {0, -1}, // left
                {0, 1},  // right
                {1, 0},  // up
                {-1, 0}  // down
        };

        for (int[] direction : directions) {
            addRookMoves(myPosition, board, listMoves, direction[0], direction[1]);
        }
    }
    private void addRookMoves(ChessPosition myPosition, ChessBoard board, Collection<ChessMove> listMoves, int rowOffset, int colOffset) {
        ChessPosition newPeez1 = myPosition;

        while (true) {
            int newRow = newPeez1.getRow() + rowOffset;
            int newCol = newPeez1.getColumn() + colOffset;

            // Check if the new position is within board limits
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                break;
            }

            newPeez1 = new ChessPosition(newRow, newCol);
            ChessMove move = new ChessMove(myPosition, newPeez1, null);
            ChessPiece pieceAtNewPosition = board.getPiece(newPeez1);

            // Add the move if the square is empty, or stop if a piece of the same color is there
            if (pieceAtNewPosition == null) {
                listMoves.add(move);
            } else {
                if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    listMoves.add(move);  // Capture move
                }
                break;  // Stop in this direction if a piece is encountered
            }
        }
    }
    public void howKnightMoves(ChessPosition myPosition, ChessBoard board, Collection<ChessMove> listMoves) {
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] offset : knightMoves) {
            addKnightMove(myPosition, board, listMoves, offset[0], offset[1]);
        }
    }
    public void howKingMoves(ChessBoard board, ChessPosition myPosition,
                             Collection<ChessMove> listMoves){
        int[][] directions = {
                {-1, 0},  // down
                {1, 0},   // up
                {0, -1},  // left
                {0, 1},   // right
                {-1, -1}, // down-left
                {-1, 1},  // down-right
                {1, -1},  // up-left
                {1, 1}    // up-right
        };

        for (int[] direction : directions) {
            int newRow = myPosition.getRow() + direction[0];
            int newCol = myPosition.getColumn() + direction[1];

            // Base case: Check if the position is out of bounds
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            ChessMove move = new ChessMove(myPosition, newPosition, null);
            // If the square is empty, add the move and stop further checks in this direction
            if (board.getPiece(newPosition) == null) {
                listMoves.add(move);
            }
            // If the square has a piece of the same color, stop further checks in this direction
            else if (board.getPiece(newPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                continue;
            }
            // If the square has an opponent's piece, add the move and stop further checks in this direction
            else if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                listMoves.add(move);
            }
        }
    }
    private void addKnightMove(ChessPosition myPosition, ChessBoard board,
                               Collection<ChessMove> listMoves, int rowOffset, int colOffset) {
        int newRow = myPosition.getRow() + rowOffset;
        int newCol = myPosition.getColumn() + colOffset;

        // Check if the position is within board limits
        if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
            return;
        }

        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessMove move = new ChessMove(myPosition, newPosition, null);

        // Check if the square is empty or has an opponent's piece
        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
        if (pieceAtNewPosition == null) {
            listMoves.add(move);
        } else if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            listMoves.add(move);
        }
    }
    public void howBishopMoves(ChessPosition myPosition, ChessBoard board, Collection<ChessMove> listMoves) {
        // Define offsets for each diagonal direction: down-left, down-right, up-left, up-right
        int[][] directions = {
                {-1, -1}, // down-left
                {-1, 1},  // down-right
                {1, -1},  // up-left
                {1, 1}    // up-right
        };
        for (int[] direction : directions) {
            addBishopMoves(myPosition, board, listMoves, direction[0], direction[1]);
        }
    }
    private void addBishopMoves(ChessPosition myPosition, ChessBoard board, Collection<ChessMove> listMoves, int rowOffset, int colOffset) {
        ChessPosition newPeez = myPosition;
        while (true) {
            int newRow = newPeez.getRow() + rowOffset;
            int newCol = newPeez.getColumn() + colOffset;
            // Check if the new position is within board limits
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                break;
            }
            newPeez = new ChessPosition(newRow, newCol);
            ChessMove move = new ChessMove(myPosition, newPeez, null);
            ChessPiece pieceAtNewPosition = board.getPiece(newPeez);
            if (pieceAtNewPosition == null) {
                listMoves.add(move);
            } else {
                if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    listMoves.add(move);  // Capture move
                }
                break;  // Stop in this direction if a piece is encountered
            }
        }
    }
    public boolean pawnWhitePromotionCheck(int row) {
        if (row == 8 || row == 1){
            return true;
        }
        return false;
    }
    public void createAllPromotionalPieces(ChessPosition myPosition, ChessPosition endingPositionition,
                                             Collection<ChessMove>listMoves) {
        ChessPiece.PieceType[]allTypes = ChessPiece.PieceType.values();
        for(ChessPiece.PieceType type: allTypes) {
            if(type == PieceType.KING || type == PieceType.PAWN) {
                continue;
            }
            ChessMove promotionMove = new ChessMove(myPosition, endingPositionition, type);
            listMoves.add(promotionMove);
        }
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove>listMoves = new ArrayList<>();
        if(this.pieceType == PieceType.PAWN){
            /* if and only if move diagonal if opposite color is on those sides
            * movement is row+1 always and depending on if there are pieces diagonally col +1 or -1
            *  */
            int newRow = myPosition.getRow();
            int newCol = myPosition.getColumn();
            boolean boolPawnWhite = false;
            if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
                boolPawnWhite = true;
            }
            if(boolPawnWhite == true) {
                //check to see if pawn has moved or not by comparing all the white pawns on the reset board in the CHESSBOARD CLASS
                if(myPosition.getRow() == 2) {
                    ChessPosition d1 = new ChessPosition(newRow+1, newCol);
                    ChessPosition d2 = new ChessPosition(newRow+2,newCol);

                    //check to see if you can move two spaces up
                    if(board.getPiece(d1) == null && board.getPiece(d2) == null) {
                        ChessPosition pNotMovedMove = new ChessPosition(newRow + 2, newCol);
                        ChessMove notMove = new ChessMove(myPosition, pNotMovedMove, null);
                        listMoves.add(notMove);
                    }
                }
                //pawn rules of movement
                howPawnMoves(board, listMoves, myPosition,
                newRow, newCol);
            }
            //else if pawn is black
            else {
                //check to see if pawn has moved or not
                if(myPosition.getRow() == 7) {
                    //base case if there is a piece in the way (white or black) 1 space up or down then continue. Not possible.
                    ChessPosition d1 = new ChessPosition(newRow-1, newCol);
                    ChessPosition d2 = new ChessPosition(newRow-2,newCol);
                    if(board.getPiece(d1) == null && board.getPiece(d2) == null) {
                        ChessPosition pNotMovedMove = new ChessPosition(newRow - 2, newCol);
                        ChessMove notMove = new ChessMove(myPosition, pNotMovedMove, null);
                        listMoves.add(notMove);
                    }
                }
                //pawn rules of movement
                howBlackPawnMoves(board, listMoves,
                        myPosition, newRow, newCol);
            }
        }
        //how king moves
        else if(this.pieceType == PieceType.KING) {
            // Directions for the king: (row delta, column delta)
            howKingMoves(board, myPosition, listMoves);
        }
        //how knight moves
        else if(this.pieceType == PieceType.KNIGHT) {
           howKnightMoves(myPosition,board,listMoves);

        }
        //how bishop moves
        else if(this.pieceType == PieceType.BISHOP) {
            howBishopMoves(myPosition,board,listMoves);
        }
        //how rook moves
        else if(this.pieceType == PieceType.ROOK){
            howRookMoves(myPosition,board,listMoves);
        }

        //how queen moves
        else if(this.pieceType == PieceType.QUEEN){
            howRookMoves(myPosition,board,listMoves);
            howBishopMoves(myPosition,board,listMoves);
        }
        return listMoves;
    }
}
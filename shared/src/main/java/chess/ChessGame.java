package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentColor;
    private TeamColor wColor;
    private TeamColor bColor;
    private ChessBoard board;
    public ChessGame() {
        this.currentColor = TeamColor.WHITE;
        this.wColor = TeamColor.WHITE;
        this.bColor = TeamColor.BLACK;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void flipSetTeamTurn(TeamColor team) {
        //just flip the colors to the opposite when this functin is called
        if(TeamColor.WHITE == team) {
            team = bColor;
        }
        else{
            team = wColor;
        }
        currentColor=team;
    }
    public void setTeamTurn(TeamColor team) {
        //just flip the colors to the opposite when this functin is called
        currentColor=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }
    public Collection<ChessMove> findValidMoves(TeamColor teamColor, ChessPiece nPeace,
                                                  Collection<ChessMove> listMovesDeleteLater) {
        // King's position
        ChessPosition kingsPosition = findKing(teamColor, board);
        // Opposite color
        TeamColor oppositeColor = flipColors(teamColor);
        // List of moves the piece can make
        List<ChessMove> movesList = new ArrayList<>(listMovesDeleteLater);
        // List of valid moves
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessBoard copyOfBoards = board.copyOfBoard();
        // Iterate through every possible move your piece and color can make,
        // make that move and use a copy of board to see if it works
        for (int num = 0; num < movesList.size(); num++) {
            ChessMove cMove = movesList.get(num);  // Chess move for your team color
            copyOfBoards = board.copyOfBoard();  // Copy the board for this move simulation
            boolean isKingInCheck = false;  // Indicates whether the move puts the king in check

            // Make the move on the copied board
            makeMoveCopy(cMove, copyOfBoards);
            ChessBoard newCopy = copyOfBoards.copyOfBoard();

            // Check if any move by the opposing pieces puts the king in check
            outerLoop:
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = copyOfBoards.getPiece(pos);

                    if (piece != null && piece.getTeamColor().equals(oppositeColor)) {
                        // Get all possible moves for the opponent's piece
                        Collection<ChessMove> opponentMoves = piece.pieceMoves(copyOfBoards, pos);
                        for (ChessMove opponentMove : opponentMoves) {
                            // If this move puts the king in check, mark it as invalid
                            //makeMoveCopy(opponentMove, copyOfBoards);
                            if (isInCheckKing(teamColor, copyOfBoards)) {
                                isKingInCheck = true;
                                break outerLoop;  // Stop checking further once the king is in check
                            }

                            // Restore board to its state before the move
                            copyOfBoards = newCopy.copyOfBoard();  // Re-copy the board to restore the state
                        }
                    }
                }
            }

            // If the move does not put the king in check, add it to the list of valid moves
            if (!isKingInCheck) {
                validMoves.add(cMove);
            }
        }

        return validMoves;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //grabs piece at board location
        ChessPiece newPiece = board.getPiece(startPosition);
        //list of moves that one piece can move
        Collection<ChessMove>emptyListMoves = new ArrayList<>();
        Collection<ChessMove>kingListMoves = new ArrayList<>();

        Collection<ChessMove>listMoves = new ArrayList<>();
        //if piece is a king
        if (newPiece != null) {
           listMoves = newPiece.pieceMoves(board,startPosition);

            if(newPiece.getPieceType().equals(ChessPiece.PieceType.KING)) {
            //if piece has not moved and is not in check go in here
            if (isInCheck(newPiece.getTeamColor()) == false) {
                //if piece moves and is not in check do this
                ChessBoard copyOfBoards = board.copyOfBoard();
                listMoves = newPiece.pieceMoves(copyOfBoards, startPosition);
                List<ChessMove>listMoves_before_change = new ArrayList<>(listMoves);
                for(int s = 0; s< listMoves_before_change.size(); s++){
                    ChessMove kingMove = listMoves_before_change.get(s);
                    makeMoveCopy(listMoves_before_change.get(s), copyOfBoards);
                    if(!isInCheckKing(newPiece.getTeamColor(), copyOfBoards)) {
                        kingListMoves.add(listMoves_before_change.get(s));
                        copyOfBoards = board.copyOfBoard();
                    }
                    copyOfBoards = board.copyOfBoard();
                }
                return kingListMoves;
            }
            else {
                listMoves = findValidMoves(newPiece.getTeamColor(), newPiece, listMoves);
                return listMoves;
            }
        }
        //if a piece is not a king
        else{
            //if piece has not moved and is not in check go in here
            if (isInCheck(newPiece.getTeamColor()) == false) {
                //if piece moves and is not in check do this
                ChessBoard copyOfBoards = board.copyOfBoard();

                List<ChessMove>newListMoves = new ArrayList<>();
                listMoves = newPiece.pieceMoves(copyOfBoards, startPosition);
                List<ChessMove>listMoves_before_change = new ArrayList<>(listMoves);

                for(int s = 0; s< listMoves_before_change.size(); s++){
                    makeMoveCopy(listMoves_before_change.get(s), copyOfBoards);
                    if(!isInCheckKing(newPiece.getTeamColor(), copyOfBoards)) {
                        //since in check, is there a move that takes a piece?
                        // if so is the king still in check after piece is taken
                        newListMoves.add(listMoves_before_change.get(s));
                    }
                    copyOfBoards = board.copyOfBoard();
                }
                return newListMoves;
            }
            else {
                //crashes here
                listMoves = findValidMoves(newPiece.getTeamColor(), newPiece, listMoves);
                return listMoves;
             }
        }
        }
        return listMoves;
    }
    boolean isInsideValidMoves(ChessMove move_check) {
        //list of moves the piece can make
        Collection<ChessMove>validMoves = validMoves(move_check.getStartPosition());
        List<ChessMove> validMovesList = new ArrayList<>(validMoves);
        //list of valid moves
        for (int num = 0; num < validMovesList.size(); num++) {
            ChessMove moveToCompare = validMovesList.get(num);
            if(validMovesList.equals(move_check)){
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard copyOfBoard = board.copyOfBoard();
        ChessPosition startingPosition = move.getStartPosition();
        ChessPosition endingPosition = move.getEndPosition();
        //make a new piece
        ChessPiece newPiece = board.getPiece(startingPosition);
        if(newPiece == null){
            throw new InvalidMoveException();
        }
        Collection<ChessMove>listPossibleMoves = newPiece.pieceMoves(board,startingPosition);
        if(!listPossibleMoves.contains(move)){
            throw new InvalidMoveException();
        }
        //is your piece in check
        if(isInCheck(newPiece.getTeamColor())==true) {
            makeMoveCopy(move, copyOfBoard);
            //is your piece in check after you move
            if(isInCheckKing(newPiece.getTeamColor(), copyOfBoard)) {
                throw new InvalidMoveException();
            }
            //throw new InvalidMoveException();
        }
        if(newPiece.getTeamColor() != currentColor){
            throw new InvalidMoveException();
        }

        //pawn promotion
//        if(move.getPromotionPiece() != null) {
//
//        }
        // Handle the special case of pawn promotion
        if (newPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            int finalRow = newPiece.getTeamColor() == TeamColor.WHITE ? 8 : 1;
            if (endingPosition.getRow() == finalRow) {
                if (move.getPromotionPiece() == null) {
                    throw new InvalidMoveException("Pawn promotion requires a promotion piece.");
                }
                // Replace the pawn with the promoted piece
                newPiece = new ChessPiece(newPiece.getTeamColor(),move.getPromotionPiece());
            }
        } else if (move.getPromotionPiece() != null) {
            // If a promotion type is provided for a non-pawn piece, it's an invalid move
            throw new InvalidMoveException("Only pawns can be promoted.");
        }





        //delete piece whether null or not, remove it. Then add piece to board
       // board.removePiece(endingPosition);
        board.addPiece(endingPosition, newPiece);
        flipSetTeamTurn(newPiece.getTeamColor());
        //delete the piece where it was at before
        board.removePiece(startingPosition);




    }
    public void makeMoveCopy(ChessMove move, ChessBoard copyOfBoard)  {
        //throw new RuntimeException("Not implemented");
        ChessPosition startingPosition = move.getStartPosition();
        ChessPosition endingPosition = move.getEndPosition();
        //make a new piece from original board
        ChessPiece newPiece = copyOfBoard.getPiece(startingPosition);
        //delete piece whether null or not, remove it. Then add piece to board
        copyOfBoard.addPiece(endingPosition, newPiece);
        //copyOfBoard.removePiece(endingPosition);
        //delete the piece where it was at before
        copyOfBoard.removePiece(startingPosition);

    }

    public TeamColor flipColors(TeamColor teamColor) {
        if (teamColor == wColor) {
            return bColor;
        }
        return wColor;
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard copy_or_og){
        ChessPiece whatImLookingFor = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingsPosition = new ChessPosition(1,1);
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++){
                //goes through the entire board to check to see if that piece is a king, and if it is return the position of the king
                ChessPosition new_pos = new ChessPosition(row, col);
                ChessPiece trueOrFalseKing = copy_or_og.getPiece(new_pos);
                if(whatImLookingFor.equals(trueOrFalseKing)){
                    //kingsPosition = new_pos;
                    return new_pos;
                }
            }
        }
        return kingsPosition;
    }



    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */


        public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        //run all possible pieces on opposite color of teamColor and see if any piece specifically hits the king, and if it does, return true
        ChessPosition kingsPosition = findKing(teamColor, board);
        //opposite color
        TeamColor oppositeColor = flipColors(teamColor);
        //all moves that lead to check
        //Collection<ChessMove> all_moves_in_check = new ArrayList<>();
        //
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition positionUlookingAt = new ChessPosition(row, col);
                //section on github of copying objects
                ChessBoard copyOfBoards = board;
                ChessPiece pieceUlookingAt = copyOfBoards.getPiece(positionUlookingAt);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                //fix this line, for some reason no likey this if statement

                if (pieceUlookingAt != null) {
                    if (pieceUlookingAt.getTeamColor().equals(oppositeColor)) {
                        pieceUlookingAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        Collection<ChessMove> listMoves = pieceUlookingAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        List<ChessMove> movesList = new ArrayList<>(listMoves);
                        for (int num = 0; num < movesList.size(); num++) {
                            ChessMove cMove = movesList.get(num);
                            if (cMove.getEndPosition().equals(kingsPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove>emptyMoves = new ArrayList<>();
        Collection<ChessMove>editedMoves = new ArrayList<>();
        ChessBoard copyOfBoard = board.copyOfBoard();

        if(isInCheckKing(teamColor, board)) {
            Collection<ChessMove> kingsMoves = king.pieceMoves(board, findKing(teamColor, board));
            List<ChessMove>kingsList = new ArrayList<>(kingsMoves);
            //all possible moves king can move
            for(int i = 0; i<1;i++) {
                ChessMove cMove = kingsList.get(i);
                editedMoves = validMoves(cMove.getStartPosition());
            }
            //if list is empty, go through all of same colors moves and see if king is in check
            //if king is not in check after move in copy board return false
            if(editedMoves.equals(emptyMoves)){
                for(int row = 1; row <=8; row++) {
                    for (int col = 1; col <= 8; col++) {
                        ChessPosition new_pos = new ChessPosition(row, col);
                        ChessPiece yourPiece = board.getPiece(new_pos);

                        if (yourPiece != null) {
                            if (yourPiece.getTeamColor().equals(teamColor)) {
                                Collection<ChessMove> yourCollection = yourPiece.pieceMoves(board, new_pos);
                                List<ChessMove> listUrPromotionalMoves = new ArrayList<>(yourCollection);
                                for (ChessMove yourMove : listUrPromotionalMoves) {
                                    makeMoveCopy(yourMove, copyOfBoard);
                                    if (!isInCheckKing(teamColor, copyOfBoard)) {
                                        return false;
                                    }
                                    copyOfBoard = board.copyOfBoard();
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }

        return false;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //if there is no moves for king and all other pieces can't move, you are in stalemate
        ChessPosition kingsPosition = findKing(teamColor,board);
        //Collection<ChessMove> kingMoves = validMoves(kingsPosition);
        //List<ChessMove>listKingMoves = new ArrayList<>(kingMoves);
        //for(ChessMove move: listKingMoves){
        Collection<ChessMove> kingMoves = validMoves(kingsPosition);
        List<ChessMove>listKingMoves = new ArrayList<>(kingMoves);
        if(listKingMoves.size() == 0){
            //if none of your other pieces can't move return true
            Collection<ChessMove> listWhitePieceMove = new ArrayList<>();
            for(int row = 1; row <=8; row++){
                for(int col =1; col <=8; col++) {
                    ChessPosition currentPosition = new ChessPosition(row, col);
                    ChessPiece curPiece = board.getPiece(currentPosition);
                    if (curPiece != null) {
                        if (curPiece.getTeamColor().equals(TeamColor.WHITE)) {
                            Collection<ChessMove> whitePieceMoves = validMoves(currentPosition);
                            if(whitePieceMoves.size()!=0) {
                                for (ChessMove move : whitePieceMoves) {
                                    listWhitePieceMove.add(move);
                                }
                            }
                            //listWhitePieceMove.add();
                        }
                    }
                }
            }
            if (listWhitePieceMove.size() == (0)) {
                return true;
            }
        }
        return false;
        //}
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        //board.resetBoard();
        return this.board;
    }

    public boolean isInCheckKing(TeamColor teamColor, ChessBoard copyOfBoards) {
        //throw new RuntimeException("Not implemented");
        //run all possible pieces on opposite color of teamColor and see if any piece specifically hits the king, and if it does, return true
        ChessPosition kingsPosition = findKing(teamColor,copyOfBoards);
        //opposite color
        TeamColor oppositeColor = flipColors(teamColor);
        //all moves that lead to check
        //Collection<ChessMove> all_moves_in_check = new ArrayList<>();
        //
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition positionUlookingAt = new ChessPosition(row, col);
                //section on github of copying objects
                //ChessBoard copyOfBoards = board;
                ChessPiece pieceUlookingAt = copyOfBoards.getPiece(positionUlookingAt);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                //fix this line, for some reason no likey this if statement

                if (pieceUlookingAt != null) {
                    if (pieceUlookingAt.getTeamColor().equals(oppositeColor)) {
                        pieceUlookingAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        Collection<ChessMove> listMoves = pieceUlookingAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        List<ChessMove> movesList = new ArrayList<>(listMoves);
                        for (int num = 0; num < movesList.size(); num++) {
                            ChessMove cMove = movesList.get(num);
                            if (cMove.getEndPosition().equals(kingsPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

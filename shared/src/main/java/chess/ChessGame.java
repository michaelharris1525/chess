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
    public boolean fixError(ChessPiece opponentPiece, ChessBoard simulatedBoard, ChessPosition pos, TeamColor teamColor){
        for (ChessMove opponentMove : opponentPiece.pieceMoves(simulatedBoard,  pos)) {
            if (isInCheckKing(teamColor, simulatedBoard)) {
               return true;
            }
        }
        return false;
    }
    public Collection<ChessMove> findValidMoves(TeamColor teamColor, ChessPiece piece, Collection<ChessMove> potentialMoves) {
        TeamColor opponentColor = flipColors(teamColor);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : potentialMoves) {
            // Copy the board once for the current move simulation
            ChessBoard simulatedBoard = board.copyOfBoard();
            makeMoveCopy(move, simulatedBoard);  // Simulate the move on the copied board

            boolean kingIsInCheck = false;

            // Loop over all squares to find opponent pieces
            outerLoop:
            for (int row = 1; row <= 8; row++) {
                kingIsInCheck = isKingIsInCheck(teamColor, row, simulatedBoard, opponentColor, kingIsInCheck);
                if(kingIsInCheck){
                    break;
                }
            }

            // Add the move to valid moves if it does not put the king in check
            if (!kingIsInCheck) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private boolean isKingIsInCheck(TeamColor teamColor, int row, ChessBoard simulatedBoard, TeamColor opponentColor, boolean kingIsInCheck) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessPiece opponentPiece = simulatedBoard.getPiece(pos);

            if (opponentPiece != null && opponentPiece.getTeamColor() == opponentColor) {
                // Check each potential move of the opponent's piece
                kingIsInCheck = fixError(opponentPiece, simulatedBoard,pos, teamColor);
                if(kingIsInCheck){
                    break;
                }
            }
        }
        return kingIsInCheck;
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

            if(newPiece.getPieceType().equals(ChessPiece.PieceType.KING) && isInCheck(newPiece.getTeamColor()) == false) {
                //if piece has not moved and is not in check go in here
                if (isInCheck(newPiece.getTeamColor()) == false) {
                    //if piece moves and is not in check do this
                    return getChessMoves(startPosition, newPiece, kingListMoves);
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
                List<ChessMove>listMovesBeforeChange = new ArrayList<>(listMoves);

                for(int s = 0; s< listMovesBeforeChange.size(); s++){
                    makeMoveCopy(listMovesBeforeChange.get(s), copyOfBoards);
                    if(!isInCheckKing(newPiece.getTeamColor(), copyOfBoards)) {
                        //since in check, is there a move that takes a piece?
                        // if so is the king still in check after piece is taken
                        newListMoves.add(listMovesBeforeChange.get(s));
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

    private Collection<ChessMove> getChessMoves(ChessPosition startPosition, ChessPiece newPiece, Collection<ChessMove> kingListMoves) {
        Collection<ChessMove> listMoves;
        ChessBoard copyOfBoards = board.copyOfBoard();
        listMoves = newPiece.pieceMoves(copyOfBoards, startPosition);
        List<ChessMove>listMovesBeforeChange = new ArrayList<>(listMoves);
        for(int s = 0; s< listMovesBeforeChange.size(); s++){
            ChessMove kingMove = listMovesBeforeChange.get(s);
            makeMoveCopy(listMovesBeforeChange.get(s), copyOfBoards);
            if(!isInCheckKing(newPiece.getTeamColor(), copyOfBoards)) {
                kingListMoves.add(listMovesBeforeChange.get(s));
                copyOfBoards = board.copyOfBoard();
            }
            copyOfBoards = board.copyOfBoard();
        }
        return kingListMoves;
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
        }
        if(newPiece.getTeamColor() != currentColor){
            throw new InvalidMoveException();
        }

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
        //delete the piece where it was at before
        copyOfBoard.removePiece(startingPosition);

    }

    public TeamColor flipColors(TeamColor teamColor) {
        if (teamColor == wColor) {
            return bColor;
        }
        return wColor;
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard copyOrOg){
        ChessPiece whatImLookingFor = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingsPosition = new ChessPosition(1,1);
        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++){
                //goes through the entire board to check to see if that piece is a king, and if it is return the position of the king
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece trueOrFalseKing = copyOrOg.getPiece(newPosition);
                if(whatImLookingFor.equals(trueOrFalseKing)){
                    //kingsPosition = newPosition;
                    return newPosition;
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
                ChessPiece pizzaLookAt = copyOfBoards.getPiece(positionUlookingAt);
                //if piece matches the oppposing player's piece then we care about if its in check or not
                //fix this line, for some reason no likey this if statement

                if (pizzaLookAt != null) {
                    if (pizzaLookAt.getTeamColor().equals(oppositeColor)) {
                        pizzaLookAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        Collection<ChessMove> listMoves = pizzaLookAt.pieceMoves(copyOfBoards, positionUlookingAt);
                        List<ChessMove> cMovesList = new ArrayList<>(listMoves);
                        for (int num = 0; num < cMovesList.size(); num++) {
                            ChessMove cMove = cMovesList.get(num);
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
                        ChessPosition newPosition = new ChessPosition(row, col);
                        ChessPiece yourPiece = board.getPiece(newPosition);

                        if (yourPiece != null) {
                            if (yourPiece.getTeamColor().equals(teamColor)) {
                                Collection<ChessMove> yourCollection = yourPiece.pieceMoves(board, newPosition);
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
        ChessPosition kingsPosition = findKing(teamColor,board);

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

        for(int row = 1; row <=8; row++) {
            for(int col = 1; col <=8; col++) {
                ChessPosition positionUlookingAt = new ChessPosition(row, col);
                ChessPiece pieceUlookingAt = copyOfBoards.getPiece(positionUlookingAt);

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

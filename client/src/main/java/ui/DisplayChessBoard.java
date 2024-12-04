package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class DisplayChessBoard {
    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    private static final String RESET_COLOR = EscapeSequences.RESET_BG_COLOR;

    private String[][] printDA = new String[8][8];
    private ChessBoard squares = new ChessBoard();
    //redo this giving a board as a private
    public DisplayChessBoard(ChessBoard chessBoard) {
//        initializeDefaultBoard();
        this.squares = chessBoard.copyOfBoard();
        printBoard();
    }

    String whatColor(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return "White";
        }
        else {
            return "Black";
        }
    }

    private void addBoard(ChessPiece piece, int row, int col){
        String color = whatColor(piece);
        if (Objects.equals(color, "White")) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                printDA[row][col] = EscapeSequences.WHITE_PAWN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                printDA[row][col] = EscapeSequences.WHITE_ROOK;
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                printDA[row][col] = EscapeSequences.WHITE_BISHOP;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                printDA[row][col] = EscapeSequences.WHITE_KING;

            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                printDA[row][col] = EscapeSequences.WHITE_QUEEN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                printDA[row][col] = EscapeSequences.WHITE_KNIGHT;
            }
        } else if (Objects.equals(color, "Black")) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                printDA[row][col] = EscapeSequences.BLACK_PAWN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                printDA[row][col] = EscapeSequences.BLACK_ROOK;
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                printDA[row][col] = EscapeSequences.BLACK_BISHOP;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                printDA[row][col] = EscapeSequences.BLACK_KING;

            } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                printDA[row][col] = EscapeSequences.BLACK_QUEEN;
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                printDA[row][col] = EscapeSequences.BLACK_KNIGHT;
            }
        }

    }

    private void printBoard(){
        for(int row=0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                ChessPosition pos = new ChessPosition(row +1, col+1);
                ChessPiece currentPiece = squares.getPiece(pos);
                if(currentPiece != null){
                    addBoard(currentPiece, row, col);
                }
                else{
                    printDA[row][col] = EscapeSequences.EMPTY;
                }
            }
        }
    }
    public void renderBoardPerspective(boolean isBlackPerspective) {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        printBoard();  // Refresh the display array

        for (int row = 0; row < 8; row++) {
            int actualRow = isBlackPerspective ? 7 - row : row;  // Perspective handling
            System.out.print(isBlackPerspective ? row + 1 : 8 - row);

            for (int col = 0; col < 8; col++) {
                int actualCol = isBlackPerspective ? 7 - col : col;
                boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;

                String piece = printDA[actualRow][actualCol];
                System.out.print(squareColor + piece + RESET_COLOR);
            }

            System.out.println(isBlackPerspective ? row + 1 : 8 - row);
        }
    }

//    private void initializeDefaultBoard() {
//        squares[0] = new String[]{
//                EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT,
//                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
//                EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP,
//                EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK
//        };
//        squares[1] = new String[]{
//                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
//                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
//                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
//                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN
//        };
//        squares[6] = new String[]{
//                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
//                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
//                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
//                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN
//        };
//        squares[7] = new String[]{
//                EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT,
//                EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
//                EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP,
//                EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK
//        };
//
//        for (int i = 2; i < 6; i++) {
//            for (int j = 0; j < 8; j++) {
//                squares[i][j] = EscapeSequences.EMPTY;
//            }
//        }
//    }

//    public void renderBoardPerspective(boolean isBlackPerspective) {
//        System.out.println(EscapeSequences.ERASE_SCREEN);
//        //System.out.print("a b c d e f g h");
//        for (int row = 0; row < 8; row++) {
//            if(!isBlackPerspective){
//                System.out.print(8 - row);//added
//            }
//            else{
//                System.out.print(row + 1);//added
//            }
//
//            int actualRow = isBlackPerspective ? 7 - row : row; // Flip rows for Black's perspective
//            for (int col = 0; col < 8; col++) {
//                int actualCol = isBlackPerspective ? 7 - col : col; // Flip columns for Black's perspective
//                boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
//                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
//
//                String piece = squares[actualRow][actualCol];
//                String pieceColor = isBlackPiece(piece) ? EscapeSequences.SET_TEXT_COLOR_GREEN : EscapeSequences.SET_TEXT_COLOR_WHITE;
//                System.out.print(squareColor + pieceColor + piece + EscapeSequences.RESET_TEXT_COLOR + RESET_COLOR);
//                if(col == 7) {
//                    if(!isBlackPerspective)  {System.out.print(8 - row); }//added
//                    else{System.out.print(row+1);}
//                }
//            }
//            System.out.println();
//        }
//    }
//
//    private boolean isBlackPiece(String piece) {
//        return piece.equals(EscapeSequences.BLACK_KING) ||
//                piece.equals(EscapeSequences.BLACK_QUEEN) ||
//                piece.equals(EscapeSequences.BLACK_ROOK) ||
//                piece.equals(EscapeSequences.BLACK_BISHOP) ||
//                piece.equals(EscapeSequences.BLACK_KNIGHT) ||
//                piece.equals(EscapeSequences.BLACK_PAWN);
//    }

}

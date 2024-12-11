package ui;

import chess.*;

import java.util.Collection;
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

    String whatColor(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return "White";
        } else {
            return "Black";
        }
    }

    private void addBoard(ChessPiece piece, int row, int col) {
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

    private void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition pos = new ChessPosition(row + 1, col + 1);
                ChessPiece currentPiece = squares.getPiece(pos);
                if (currentPiece != null) {
                    addBoard(currentPiece, row, col);
                } else {
                    printDA[row][col] = EscapeSequences.EMPTY;
                }
            }
        }
    }
    public void renderBoardPerspective(boolean isBlackPerspective) {
        System.out.println(EscapeSequences.ERASE_SCREEN);
        printBoard();  // Refresh the display array

        // Print column labels (top border)
        //System.out.print("  "); // Offset for row numbers
        for (int col = 0; col < 8; col++) {
            char columnLabel = (char) ('a' + (isBlackPerspective ? 7 - col : col));
            System.out.print("  " + columnLabel + " ");
        }
        System.out.println();

        // Print the board
        for (int row = 0; row < 8; row++) {
            int actualRow = isBlackPerspective ? 7 - row : row;  // Perspective handling
            System.out.print(isBlackPerspective ? row + 1 : 8 - row); // Row numbers (left side)

            for (int col = 0; col < 8; col++) {
                int actualCol = isBlackPerspective ? 7 - col : col;
                boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;

                String piece = printDA[actualRow][actualCol];
                System.out.print(squareColor + piece + RESET_COLOR);
            }

            System.out.println(" " + (isBlackPerspective ? row + 1 : 8 - row)); // Row numbers (right side)
        }

        // Print column labels (bottom border)
        //System.out.print("  "); // Offset for row numbers
        for (int col = 0; col < 8; col++) {
            char columnLabel = (char) ('a' + (isBlackPerspective ? 7 - col : col));
            System.out.print("  " + columnLabel + " ");
        }
        System.out.println();
    }


//    public void renderBoardPerspective(boolean isBlackPerspective) {
//        System.out.println(EscapeSequences.ERASE_SCREEN);
//        printBoard();  // Refresh the display array
//
//        for (int row = 0; row < 8; row++) {
//            int actualRow = isBlackPerspective ? 7 - row : row;  // Perspective handling
//            System.out.print(isBlackPerspective ? row + 1 : 8 - row);
//
//            for (int col = 0; col < 8; col++) {
//                int actualCol = isBlackPerspective ? 7 - col : col;
//                boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
//                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
//
//                String piece = printDA[actualRow][actualCol];
//                System.out.print(squareColor + piece + RESET_COLOR);
//            }
//
//            System.out.println(isBlackPerspective ? row + 1 : 8 - row);
//        }
//    }
}
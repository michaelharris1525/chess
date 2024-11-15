package ui;

public class ChessBoard {
    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String RESET_COLOR = EscapeSequences.RESET_BG_COLOR;

    private String[][] squares = new String[8][8]; // Change type to String[][]

    public ChessBoard() {
        initializeDefaultBoard();
    }

    private void initializeDefaultBoard() {
        squares[0] = new String[]{
                EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP,
                EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP,
                EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK
        };
        squares[1] = new String[]{
                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
                EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN
        };
        squares[6] = new String[]{
                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
                EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN
        };
        squares[7] = new String[]{
                EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP,
                EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP,
                EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK
        };

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = EscapeSequences.EMPTY;
            }
        }
    }

//    public void renderBoard() {
//        System.out.println(EscapeSequences.ERASE_SCREEN);
//        for (int row = 0; row < 8; row++) {
//            for (int col = 0; col < 8; col++) {
//                boolean isLightSquare = (row + col) % 2 == 0;
//                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
//                System.out.print(squareColor + squares[row][col] + RESET_COLOR);
//            }
//            System.out.println();
//        }
//    }
//    public void renderBoardPerspective(boolean isBlackPerspective) {
//        System.out.println(EscapeSequences.ERASE_SCREEN);
//        for (int row = 0; row < 8; row++) {
//            int actualRow = isBlackPerspective ? 7 - row : row; // Flip rows for Black's perspective
//            for (int col = 0; col < 8; col++) {
//                int actualCol = isBlackPerspective ? 7 - col : col; // Flip columns for Black's perspective
//                boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
//                String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;
//                System.out.print(squareColor + squares[actualRow][actualCol] + RESET_COLOR); // Use directly
//            }
//            System.out.println();
//        }
//    }
public void renderBoard(boolean isBlackPerspective) {
    System.out.println(EscapeSequences.ERASE_SCREEN);
    for (int row = 0; row < 8; row++) {
        int actualRow = isBlackPerspective ? 7 - row : row; // Flip rows for Black's perspective
        for (int col = 0; col < 8; col++) {
            int actualCol = isBlackPerspective ? 7 - col : col; // Flip columns for Black's perspective
            boolean isLightSquare = (actualRow + actualCol) % 2 == 0;
            String squareColor = isLightSquare ? LIGHT_SQUARE : DARK_SQUARE;

            String piece = squares[actualRow][actualCol];
            String pieceColor = isBlackPiece(piece) ? EscapeSequences.SET_TEXT_COLOR_GREEN : EscapeSequences.SET_TEXT_COLOR_WHITE;

            System.out.print(squareColor + pieceColor + piece + EscapeSequences.RESET_TEXT_COLOR + RESET_COLOR);
        }
        System.out.println();
    }
}
}

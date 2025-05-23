package com.cnpm.gameapp.games.sodoku;

public class SudokuPuzzle {
    protected String[][] board;
    protected boolean[][] mutable;
    private final int ROWS;
    private final int COLUMNS;
    private final int BOXWIDTH;
    private final int BOXHEIGHT;
    private final String[] VALIDVALUES;

    public SudokuPuzzle(int rows, int columns, int boxWidth, int boxHeight, String[] validValues) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.BOXWIDTH = boxWidth;
        this.BOXHEIGHT = boxHeight;
        this.VALIDVALUES = validValues;
        this.board = new String[ROWS][COLUMNS];
        this.mutable = new boolean[ROWS][COLUMNS];
        initializeBoard();
        initializeMutableSlots();
    }

    public SudokuPuzzle(SudokuPuzzle puzzle) {
        this.ROWS = puzzle.ROWS;
        this.COLUMNS = puzzle.COLUMNS;
        this.BOXWIDTH = puzzle.BOXWIDTH;
        this.BOXHEIGHT = puzzle.BOXHEIGHT;
        this.VALIDVALUES = puzzle.VALIDVALUES;
        this.board = new String[ROWS][COLUMNS];
        this.mutable = new boolean[ROWS][COLUMNS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                board[r][c] = puzzle.board[r][c];
                mutable[r][c] = puzzle.mutable[r][c];
            }
        }
    }

    public int getNumRows() {
        return ROWS;
    }

    public int getNumColumns() {
        return COLUMNS;
    }

    public int getBoxWidth() {
        return BOXWIDTH;
    }

    public int getBoxHeight() {
        return BOXHEIGHT;
    }

    public String[] getValidValues() {
        return VALIDVALUES;
    }

    public boolean makeMove(int row, int col, String value, boolean isMutable) {
        if (isValidValue(value) && isSlotMutable(row, col)) {
            board[row][col] = value;
            mutable[row][col] = isMutable;
            return true;
        }
        return false;
    }

    public boolean isValidMove(int row, int col, String value) {
        return inRange(row, col) && !numInCol(col, value) && !numInRow(row, value) && !numInBox(row, col, value);
    }

    public boolean numInCol(int col, String value) {
        for (int row = 0; row < ROWS; row++) {
            if (board[row][col].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean numInRow(int row, String value) {
        for (int col = 0; col < COLUMNS; col++) {
            if (board[row][col].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean numInBox(int row, int col, String value) {
        int boxRow = row / BOXHEIGHT;
        int boxCol = col / BOXWIDTH;
        int startingRow = boxRow * BOXHEIGHT;
        int startingCol = boxCol * BOXWIDTH;

        for (int r = startingRow; r < startingRow + BOXHEIGHT; r++) {
            for (int c = startingCol; c < startingCol + BOXWIDTH; c++) {
                if (board[r][c].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSlotAvailable(int row, int col) {
        return inRange(row, col) && board[row][col].isEmpty() && isSlotMutable(row, col);
    }

    public boolean isSlotMutable(int row, int col) {
        return inRange(row, col) && mutable[row][col];
    }

    public String getValue(int row, int col) {
        return inRange(row, col) ? board[row][col] : "";
    }

    public boolean boardFull() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (board[r][c].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeSlotEmpty(int row, int col) {
        board[row][col] = "";
    }

    public boolean isValidSolution() {
        for (int row = 0; row < ROWS; row++) {
            boolean[] seen = new boolean[VALIDVALUES.length];
            for (int col = 0; col < COLUMNS; col++) {
                String value = board[row][col];
                if (!value.isEmpty()) {
                    if (!isValidValue(value)) return false;
                    int index = getValueIndex(value);
                    if (index == -1 || seen[index]) return false;
                    seen[index] = true;
                }
            }
        }

        for (int col = 0; col < COLUMNS; col++) {
            boolean[] seen = new boolean[VALIDVALUES.length];
            for (int row = 0; row < ROWS; row++) {
                String value = board[row][col];
                if (!value.isEmpty()) {
                    if (!isValidValue(value)) return false;
                    int index = getValueIndex(value);
                    if (index == -1 || seen[index]) return false;
                    seen[index] = true;
                }
            }
        }

        for (int startRow = 0; startRow < ROWS; startRow += BOXHEIGHT) {
            for (int startCol = 0; startCol < COLUMNS; startCol += BOXWIDTH) {
                boolean[] seen = new boolean[VALIDVALUES.length];
                for (int r = startRow; r < startRow + BOXHEIGHT; r++) {
                    for (int c = startCol; c < startCol + BOXWIDTH; c++) {
                        String value = board[r][c];
                        if (!value.isEmpty()) {
                            if (!isValidValue(value)) return false;
                            int index = getValueIndex(value);
                            if (index == -1 || seen[index]) return false;
                            seen[index] = true;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean isValidValue(String value) {
        for (String str : VALIDVALUES) {
            if (str.equals(value)) return true;
        }
        return false;
    }

    boolean inRange(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    private int getValueIndex(String value) {
        for (int i = 0; i < VALIDVALUES.length; i++) {
            if (VALIDVALUES[i].equals(value)) return i;
        }
        return -1;
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = "";
            }
        }
    }

    private void initializeMutableSlots() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                mutable[row][col] = true;
            }
        }
    }
}
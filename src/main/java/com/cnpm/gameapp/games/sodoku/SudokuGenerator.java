package com.cnpm.gameapp.games.sodoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {
    private final Random random = new Random();

    public SudokuPuzzle generateRandomSudoku(SudokuPuzzleType puzzleType) {
        SudokuPuzzle puzzle = new SudokuPuzzle(puzzleType.getRows(), puzzleType.getColumns(), puzzleType.getBoxWidth(),
                puzzleType.getBoxHeight(), puzzleType.getValidValues());
        SudokuPuzzle copy = new SudokuPuzzle(puzzle);

        // Fill the first column with random valid values
        List<String> notUsedValidValues = new ArrayList<>(Arrays.asList(copy.getValidValues()));
        for (int r = 0; r < copy.getNumRows(); r++) {
            int randomIndex = random.nextInt(notUsedValidValues.size());
            copy.makeMove(r, 0, notUsedValidValues.get(randomIndex), true);
            notUsedValidValues.remove(randomIndex);
        }

        // Solve the puzzle to get a complete board
        backtrackSudokuSolver(0, 0, copy);

        // Keep a fraction of the values to create the puzzle
        int numberOfValuesToKeep = (int) (0.22222 * (copy.getNumRows() * copy.getNumRows()));
        for (int i = 0; i < numberOfValuesToKeep;) {
            int randomRow = random.nextInt(puzzle.getNumRows());
            int randomColumn = random.nextInt(puzzle.getNumColumns());

            if (puzzle.isSlotAvailable(randomRow, randomColumn)) {
                puzzle.makeMove(randomRow, randomColumn, copy.getValue(randomRow, randomColumn), false);
                i++;
            }
        }

        return puzzle;
    }

    private boolean backtrackSudokuSolver(int r, int c, SudokuPuzzle puzzle) {
        if (!puzzle.inRange(r, c)) return false;

        if (puzzle.isSlotAvailable(r, c)) {
            for (String value : puzzle.getValidValues()) {
                if (!puzzle.numInRow(r, value) && !puzzle.numInCol(c, value) && !puzzle.numInBox(r, c, value)) {
                    puzzle.makeMove(r, c, value, true);

                    if (puzzle.boardFull()) return true;

                    int nextRow = r == puzzle.getNumRows() - 1 ? 0 : r + 1;
                    int nextCol = r == puzzle.getNumRows() - 1 ? c + 1 : c;

                    if (backtrackSudokuSolver(nextRow, nextCol, puzzle)) return true;

                    puzzle.makeSlotEmpty(r, c);
                }
            }
            return false;
        } else {
            int nextRow = r == puzzle.getNumRows() - 1 ? 0 : r + 1;
            int nextCol = r == puzzle.getNumRows() - 1 ? c + 1 : c;
            return backtrackSudokuSolver(nextRow, nextCol, puzzle);
        }
    }
}
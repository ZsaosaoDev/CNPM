package com.cnpm.gameapp.games.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToePanel extends JPanel {
    private static final int SIZE = 20; // Board size
    private final JButton[][] cells = new JButton[SIZE][SIZE];
    private boolean xTurn = true; // X for player, O for computer
    private JLabel statusLabel;
    private JLabel aiThinkingLabel; // Label for AI thinking status
    private boolean isAgainstComputer = false; // PvC mode flag

    public TicTacToePanel(CardLayout layout, JPanel container) {
        setLayout(new BorderLayout());
        showModeSelectionScreen(layout, container); // Show mode selection
    }

    // Mode selection screen (PvP or PvC)
    private void showModeSelectionScreen(CardLayout layout, JPanel container) {
        JPanel modeSelectionPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎮 CHỌN CHẾ ĐỘ CHƠI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        modeSelectionPanel.add(titleLabel, BorderLayout.NORTH);

        JButton pvpButton = new JButton("PvP (Người chơi vs Người chơi)");
        pvpButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pvpButton.addActionListener(e -> {
            isAgainstComputer = false;
            startGame(layout, container);
        });

        JButton pvcButton = new JButton("PvC (Người chơi vs Máy)");
        pvcButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pvcButton.addActionListener(e -> {
            isAgainstComputer = true;
            startGame(layout, container);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pvpButton);
        buttonPanel.add(pvcButton);
        modeSelectionPanel.add(buttonPanel, BorderLayout.CENTER);
        add(modeSelectionPanel, BorderLayout.CENTER);
    }

    // Start the game after mode selection
    private void startGame(CardLayout layout, JPanel container) {
        removeAll();
        xTurn = true;

        // Status label
        statusLabel = new JLabel("Lượt: ❌", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        // AI thinking label
        aiThinkingLabel = new JLabel("", SwingConstants.CENTER);
        aiThinkingLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));

        // North panel for status and AI thinking
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(statusLabel, BorderLayout.NORTH);
        northPanel.add(aiThinkingLabel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // Game board
        JPanel board = new JPanel(new GridLayout(SIZE, SIZE));
        Font cellFont = new Font("SansSerif", Font.BOLD, 16);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(30, 30));
                btn.setFont(cellFont);
                final int r = row, c = col;
                btn.addActionListener(e -> handleMove(r, c));
                cells[row][col] = btn;
                board.add(btn);
            }
        }
        add(board, BorderLayout.CENTER);

        // Back to menu button
        JButton backButton = new JButton("⬅ Quay lại menu");
        backButton.addActionListener(e -> layout.show(container, "menu"));

        // Change mode button
        JButton changeModeButton = new JButton("🔄 Chọn lại chế độ");
        changeModeButton.addActionListener(e -> {
            removeAll();
            showModeSelectionScreen(layout, container);
            revalidate();
            repaint();
        });

        // South panel for buttons
        JPanel southPanel = new JPanel();
        southPanel.add(backButton);
        southPanel.add(changeModeButton);
        add(southPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // Handle player's move
    private void handleMove(final int row, final int col) {
        JButton cell = cells[row][col];
        if (!cell.getText().isEmpty()) return;

        // Update UI immediately with player's move
        String mark = xTurn ? "❌" : "⭕";
        cell.setText(mark);

        if (checkWin(row, col, mark)) {
            JOptionPane.showMessageDialog(this, "🎉 " + mark + " thắng!");
            resetBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "🏁 Hòa!");
            resetBoard();
        } else {
            xTurn = !xTurn;
            statusLabel.setText("Lượt: " + (xTurn ? "❌" : "⭕"));

            if (isAgainstComputer && !xTurn) {
                // Disable board to prevent player moves during AI thinking
                setBoardEnabled(false);
                // Show AI thinking message
                aiThinkingLabel.setText("🤖 AI đang suy nghĩ...");

                // Run AI move in background
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        computerMove();
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Re-enable board and clear thinking message
                        setBoardEnabled(true);
                        aiThinkingLabel.setText("");
                    }
                }.execute();
            }
        }
    }

    // Enable or disable the board
    private void setBoardEnabled(boolean enabled) {
        for (JButton[] row : cells) {
            for (JButton btn : row) {
                btn.setEnabled(enabled);
            }
        }
    }

    // Computer's move with immediate win/block check
    private void computerMove() {
        // Check for immediate win
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    cells[row][col].setText("⭕");
                    if (checkWin(row, col, "⭕")) {
                        final int finalRow = row; // Create final copy
                        final int finalCol = col; // Create final copy
                        SwingUtilities.invokeLater(() -> {
                            cells[finalRow][finalCol].setText("⭕"); // Use final copies
                            JOptionPane.showMessageDialog(this, "🎉 Máy thắng!");
                            resetBoard();
                        });
                        return;
                    }
                    cells[row][col].setText("");
                }
            }
        }

        // Check for immediate block
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    cells[row][col].setText("❌");
                    if (checkWin(row, col, "❌")) {
                        final int finalRow = row; // Create final copy
                        final int finalCol = col; // Create final copy
                        SwingUtilities.invokeLater(() -> {
                            cells[finalRow][finalCol].setText("⭕"); // Use final copies
                            if (isBoardFull()) {
                                JOptionPane.showMessageDialog(this, "🏁 Hòa!");
                                resetBoard();
                            } else {
                                xTurn = true;
                                statusLabel.setText("Lượt: ❌");
                            }
                        });
                        return;
                    }
                    cells[row][col].setText("");
                }
            }
        }

        // Use Minimax for other moves
        int[] bestMove = minimax(4); // Depth 4
        int row = bestMove[0];
        int col = bestMove[1];

        SwingUtilities.invokeLater(() -> {
            cells[row][col].setText("⭕");

            if (checkWin(row, col, "⭕")) {
                JOptionPane.showMessageDialog(this, "🎉 Máy thắng!");
                resetBoard();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "🏁 Hòa!");
                resetBoard();
            } else {
                xTurn = true;
                statusLabel.setText("Lượt: ❌");
            }
        });
    }

    // Minimax algorithm to find the best move
    private int[] minimax(int depthLimit) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        List<int[]> possibleMoves = getPossibleMoves();
        if (possibleMoves.isEmpty()) {
            return new int[]{SIZE / 2, SIZE / 2}; // Fallback to center
        }

        for (int[] move : possibleMoves) {
            int row = move[0];
            int col = move[1];
            cells[row][col].setText("⭕"); // Simulate move
            int score = minimaxScore(depthLimit - 1, false, alpha, beta, row, col);
            cells[row][col].setText(""); // Undo move

            if (score > bestScore) {
                bestScore = score;
                bestMove[0] = row;
                bestMove[1] = col;
            }
            alpha = Math.max(alpha, bestScore);
            if (beta <= alpha) {
                break; // Alpha-beta pruning
            }
        }

        if (bestMove[0] == -1 || bestMove[1] == -1) {
            bestMove = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        }

        return bestMove;
    }

    // Recursive Minimax scoring with alpha-beta pruning
    private int minimaxScore(int depth, boolean isMaximizingPlayer, int alpha, int beta, int lastRow, int lastCol) {
        // Check for win at the last move
        if (lastRow >= 0 && lastCol >= 0 && !cells[lastRow][lastCol].getText().isEmpty()) {
            String mark = cells[lastRow][lastCol].getText();
            if (checkWin(lastRow, lastCol, mark)) {
                return mark.equals("⭕") ? 10000 - depth : -10000 + depth;
            }
        }

        if (isBoardFull() || depth == 0) {
            return evaluateBoard(); // Heuristic evaluation for non-terminal states
        }

        List<int[]> possibleMoves = getPossibleMoves();

        if (isMaximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                int row = move[0];
                int col = move[1];
                cells[row][col].setText("⭕"); // Simulate computer move
                int score = minimaxScore(depth - 1, false, alpha, beta, row, col);
                cells[row][col].setText(""); // Undo move
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, maxScore);
                if (beta <= alpha) {
                    break; // Alpha-beta pruning
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                int row = move[0];
                int col = move[1];
                cells[row][col].setText("❌"); // Simulate player move
                int score = minimaxScore(depth - 1, true, alpha, beta, row, col);
                cells[row][col].setText(""); // Undo move
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, minScore);
                if (beta <= alpha) {
                    break; // Alpha-beta pruning
                }
            }
            return minScore;
        }
    }

    // Heuristic evaluation for non-terminal states
    private int evaluateBoard() {
        int score = 0;

        // Evaluate all directions for both players
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!cells[row][col].getText().isEmpty()) {
                    String mark = cells[row][col].getText();
                    boolean isComputer = mark.equals("⭕");
                    score += evaluateDirection(row, col, mark, 1, 0, isComputer); // Horizontal
                    score += evaluateDirection(row, col, mark, 0, 1, isComputer); // Vertical
                    score += evaluateDirection(row, col, mark, 1, 1, isComputer); // Diagonal \
                    score += evaluateDirection(row, col, mark, 1, -1, isComputer); // Diagonal /
                }
            }
        }

        return score;
    }

    // Evaluate a direction for sequences of marks
    private int evaluateDirection(int row, int col, String mark, int dx, int dy, boolean isComputer) {
        int count = 0;
        int openEnds = 0;
        int i;

        // Count marks in positive direction
        for (i = 1; i < 5; i++) {
            int r = row + dx * i;
            int c = col + dy * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !cells[r][c].getText().equals(mark)) {
                if (r >= 0 && c >= 0 && r < SIZE && c < SIZE && cells[r][c].getText().isEmpty()) {
                    openEnds++;
                }
                break;
            }
            count++;
        }

        // Count marks in negative direction
        for (i = 1; i < 5; i++) {
            int r = row - dx * i;
            int c = col - dy * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !cells[r][c].getText().equals(mark)) {
                if (r >= 0 && c >= 0 && r < SIZE && c < SIZE && cells[r][c].getText().isEmpty()) {
                    openEnds++;
                }
                break;
            }
            count++;
        }

        // Score based on sequence length
        int score = 0;
        if (count >= 4) score = isComputer ? 5000 : -5000; // 4 in a row is critical
        else if (count == 3 && openEnds > 0) score = isComputer ? 100 : -100; // 3 in a row with open end
        else if (count == 2 && openEnds > 0) score = isComputer ? 10 : -10; // 2 in a row with open end
        else if (count == 1 && openEnds > 0) score = isComputer ? 1 : -1; // Single mark with open end

        return score;
    }

    // Get list of possible moves (prioritize adjacent cells and threats)
    private List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        boolean hasAdjacent = false;

        // First, check for moves that create or block 4-in-a-row (critical threats)
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    // Check if placing ⭕ creates a win
                    cells[row][col].setText("⭕");
                    if (checkWin(row, col, "⭕")) {
                        cells[row][col].setText("");
                        moves.add(new int[]{row, col});
                        return moves; // Immediate win, return it
                    }
                    cells[row][col].setText("");

                    // Check if placing ❌ creates a win (to block)
                    cells[row][col].setText("❌");
                    if (checkWin(row, col, "❌")) {
                        cells[row][col].setText("");
                        moves.add(new int[]{row, col});
                        continue; // Block move, add but keep checking
                    }
                    cells[row][col].setText("");

                    // Add adjacent moves
                    if (hasNeighbor(row, col)) {
                        moves.add(new int[]{row, col});
                        hasAdjacent = true;
                    }
                }
            }
        }

        // If no critical threats, return adjacent moves
        if (hasAdjacent) {
            return moves;
        }

        // Fallback to all empty cells if no adjacent moves
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    moves.add(new int[]{row, col});
                }
            }
        }

        return moves;
    }

    // Check if a cell has a non-empty neighbor
    private boolean hasNeighbor(int row, int col) {
        for (int dr = -2; dr <= 2; dr++) { // Extended range for 5-in-a-row
            for (int dc = -2; dc <= 2; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE && !cells[nr][nc].getText().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Check if the board is full
    private boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check for a win at the given position
    private boolean checkWin(int row, int col, String mark) {
        return checkDir(row, col, mark, 1, 0) || // Horizontal
                checkDir(row, col, mark, 0, 1) || // Vertical
                checkDir(row, col, mark, 1, 1) || // Diagonal \
                checkDir(row, col, mark, 1, -1);  // Diagonal /
    }

    // Check a direction for 5-in-a-row
    private boolean checkDir(int row, int col, String mark, int dx, int dy) {
        int count = 1;
        count += countDirection(row, col, mark, dx, dy);
        count += countDirection(row, col, mark, -dx, -dy);
        return count >= 5;
    }

    // Count marks in one direction
    private int countDirection(int row, int col, String mark, int dx, int dy) {
        int count = 0;
        for (int step = 1; step < 5; step++) {
            int r = row + dx * step;
            int c = col + dy * step;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE) break;
            if (mark.equals(cells[r][c].getText())) count++;
            else break;
        }
        return count;
    }

    // Reset the board
    private void resetBoard() {
        for (JButton[] row : cells) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true); // Ensure buttons are re-enabled
            }
        }
        xTurn = true;
        statusLabel.setText("Lượt: ❌");
        aiThinkingLabel.setText(""); // Clear AI thinking message
    }
}
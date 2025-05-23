package com.cnpm.gameapp.games.minesweeper;

import com.cnpm.gameapp.core.GamePanel;
import com.cnpm.gameapp.core.NavigationButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperPanel implements GamePanel {
    private static final int SIZE = 10;
    private final JPanel panel = new JPanel(new BorderLayout());
    private final JButton[][] cells = new JButton[SIZE][SIZE];
    private final MinesweeperLogic logic;
    private final MinesweeperTimer timer;
    private JLabel statusLabel;

    // Define colors for better contrast
    private static final Color UNREVEALED_COLOR = new Color(239, 233, 233); // Light gray
    private static final Color REVEALED_COLOR = new Color(255, 255, 255); // Off-white
    private static final Color FLAGGED_COLOR = new Color(255, 204, 0); // Yellow
    private static final Color MINE_COLOR = new Color(255, 102, 102); // Red
    private static final Color[] NUMBER_COLORS = {
            Color.BLUE,      // 1
            new Color(0, 128, 0), // 2 (Green)
            Color.RED,       // 3
            new Color(128, 0, 128), // 4 (Purple)
            new Color(139, 69, 19), // 5 (Brown)
            Color.CYAN,      // 6
            Color.BLACK,     // 7
            Color.DARK_GRAY  // 8
    };

    public MinesweeperPanel(CardLayout layout, JPanel container) {
        logic = new MinesweeperLogic(SIZE);
        timer = new MinesweeperTimer(this::updateStatusLabel);
        showModeSelectionScreen(layout, container);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void resetGame() {
        logic.initializeGame(timer.getMinesCount());
        timer.reset();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
                cells[row][col].setEnabled(true);
                cells[row][col].setBackground(UNREVEALED_COLOR);
                cells[row][col].setForeground(Color.BLACK);
            }
        }
        statusLabel.setText("Trò chơi bắt đầu!");
        timer.start();
    }

    private void showModeSelectionScreen(CardLayout layout, JPanel container) {
        panel.removeAll();
        JPanel modeSelectionPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎮 CHỌN CHẾ ĐỘ CHƠI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        modeSelectionPanel.add(titleLabel, BorderLayout.NORTH);

        JRadioButton easyButton = new JRadioButton("Dễ");
        JRadioButton hardButton = new JRadioButton("Khó");
        ButtonGroup difficultyGroup = new ButtonGroup();
        difficultyGroup.add(easyButton);
        difficultyGroup.add(hardButton);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.add(easyButton);
        difficultyPanel.add(hardButton);

        JButton startButton = new JButton("Bắt đầu trò chơi");
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        startButton.addActionListener(e -> {
            if (!easyButton.isSelected() && !hardButton.isSelected()) {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn độ khó trước khi bắt đầu!",
                        "Chưa chọn độ khó!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            timer.setMinesCount(hardButton.isSelected() ? 25 : 10);
            startGame(layout, container);
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(difficultyPanel, BorderLayout.NORTH);
        centerPanel.add(startButton, BorderLayout.CENTER);

        modeSelectionPanel.add(centerPanel, BorderLayout.CENTER);
        panel.add(modeSelectionPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void startGame(CardLayout layout, JPanel container) {
        panel.removeAll();
        logic.initializeGame(timer.getMinesCount());

        statusLabel = new JLabel("Trò chơi bắt đầu!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(statusLabel, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);

        timer.start();

        JPanel board = new JPanel(new GridLayout(SIZE, SIZE, 1, 1));
        board.setBackground(Color.BLACK); // Grid lines
        Font cellFont = new Font("SansSerif", Font.BOLD, 16);

        int cellSize = calculateCellSize(timer.getMinesCount());

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(cellSize, cellSize));
                btn.setMaximumSize(new Dimension(cellSize, cellSize));
                btn.setMinimumSize(new Dimension(cellSize, cellSize));
                btn.setFont(cellFont);
                btn.setBackground(UNREVEALED_COLOR);
                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                final int r = row, c = col;

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            logic.toggleFlag(r, c);
                            updateCellVisual(r, c);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            handleMove(r, c);
                        }
                    }
                });

                cells[row][col] = btn;
                board.add(btn);
            }
        }
        panel.add(board, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.add(NavigationButton.create("⬅ Quay lại menu", "menu", layout, container));

        JButton restartButton = NavigationButton.create("🔄 Chơi lại", null, layout, container);
        restartButton.addActionListener(e -> startGame(layout, container));
        southPanel.add(restartButton);

        JButton difficultyButton = NavigationButton.create("🔄 Chọn lại độ khó", null, layout, container);
        difficultyButton.addActionListener(e -> showModeSelectionScreen(layout, container));
        southPanel.add(difficultyButton);

        panel.add(southPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void handleMove(int row, int col) {
        if (!logic.handleMove(row, col)) {
            timer.stop();
            statusLabel.setText("💥 Bạn đã thua! Trò chơi kết thúc.");
            revealAllMines();
        } else {
            updateBoardVisual();
            if (logic.checkWin()) {
                timer.stop();
                long score = timer.getScore();
                statusLabel.setText(
                        "🎉 Bạn đã thắng! Chúc mừng! ⏱ " + timer.getElapsedTime() + " giây | 🏆 Điểm: " + score);
            }
        }
    }

    private void updateCellVisual(int row, int col) {
        JButton cell = cells[row][col];
        if (logic.isFlagged(row, col)) {
            cell.setText("🚩");
            cell.setBackground(FLAGGED_COLOR);
            cell.setForeground(Color.BLACK);
        } else if (logic.isRevealed(row, col)) {
            int mines = logic.getNeighboringMines(row, col);
            cell.setText(mines == 0 ? "" : String.valueOf(mines));
            cell.setBackground(REVEALED_COLOR);
            cell.setForeground(mines > 0 ? NUMBER_COLORS[mines - 1] : Color.BLACK);
            cell.setEnabled(false);
        } else {
            cell.setText("");
            cell.setBackground(UNREVEALED_COLOR);
            cell.setForeground(Color.BLACK);
        }
    }

    private void updateBoardVisual() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                updateCellVisual(row, col);
            }
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (logic.isMine(row, col)) {
                    cells[row][col].setText("💣");
                    cells[row][col].setBackground(MINE_COLOR);
                    cells[row][col].setForeground(Color.BLACK);
                    cells[row][col].setEnabled(false);
                } else {
                    updateCellVisual(row, col);
                }
            }
        }
    }

    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
    }

    /**
     * Calculate cell size based on number of mines (difficulty)
     *
     * @param minesCount Number of mines
     * @return Appropriate cell size
     */
    private int calculateCellSize(int minesCount) {
        return minesCount <= 10 ? 40 : 35;
    }
}
package com.cnpm.gameapp.games.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperPanel extends JPanel {
    private static final int SIZE = 10;
    private final JButton[][] cells = new JButton[SIZE][SIZE];
    private boolean[][] mines = new boolean[SIZE][SIZE];
    private boolean[][] revealed = new boolean[SIZE][SIZE];
    private boolean[][] flagged = new boolean[SIZE][SIZE];
    private JLabel statusLabel;

    private int minesCount = 10;

    // ⏱️ Thêm biến thời gian và bộ đếm
    private Timer timer;
    private long startTime;
    private long elapsedTime;

    public MinesweeperPanel(CardLayout layout, JPanel container) {
        setLayout(new BorderLayout());
        showModeSelectionScreen(layout, container);
    }

    private void showModeSelectionScreen(CardLayout layout, JPanel container) {
        removeAll();
        JPanel modeSelectionPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎮 CHỌN CHẾ ĐỘ CHƠI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        modeSelectionPanel.add(titleLabel, BorderLayout.NORTH);

        JRadioButton easyButton = new JRadioButton("Dễ                 ");
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
                JOptionPane.showMessageDialog(this, "Vui lòng chọn độ khó trước khi bắt đầu!", "Chưa chọn độ khó!", JOptionPane.WARNING_MESSAGE);
                return;
            }

            minesCount = hardButton.isSelected() ? 25 : 10;
            startGame(layout, container);
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(difficultyPanel, BorderLayout.NORTH);
        centerPanel.add(startButton, BorderLayout.CENTER);

        modeSelectionPanel.add(centerPanel, BorderLayout.CENTER);
        add(modeSelectionPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void startGame(CardLayout layout, JPanel container) {
        removeAll();
        initializeGame();

        statusLabel = new JLabel("Trò chơi bắt đầu!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(statusLabel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // ⏱️ Khởi động bộ đếm thời gian
        startTime = System.currentTimeMillis();
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
        updateTimer();

        JPanel board = new JPanel(new GridLayout(SIZE, SIZE));
        Font cellFont = new Font("SansSerif", Font.BOLD, 16);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                btn.setMaximumSize(new Dimension(40, 40));
                btn.setMinimumSize(new Dimension(40, 40));
                btn.setFont(cellFont);
                final int r = row, c = col;

                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(r, c);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            handleMove(r, c);
                        }
                    }
                });

                cells[row][col] = btn;
                board.add(btn);
            }
        }
        add(board, BorderLayout.CENTER);

        JButton backButton = new JButton("⬅ Quay lại menu");
        backButton.addActionListener(e -> layout.show(container, "menu"));

        JButton replayButton = new JButton("🔄 Chơi lại");
        replayButton.addActionListener(e -> startGame(layout, container));

        JButton difficultyButton = new JButton("🔄 Chọn lại độ khó");
        difficultyButton.addActionListener(e -> showModeSelectionScreen(layout, container));

        JPanel southPanel = new JPanel();
        southPanel.add(backButton);
        southPanel.add(replayButton);
        southPanel.add(difficultyButton);
        add(southPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void initializeGame() {
        mines = new boolean[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];

        Random random = new Random();
        int placed = 0;
        while (placed < minesCount) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (!mines[row][col]) {
                mines[row][col] = true;
                placed++;
            }
        }
    }

    private void handleMove(int row, int col) {
        if (revealed[row][col] || flagged[row][col]) return;

        revealed[row][col] = true;
        if (mines[row][col]) {
            statusLabel.setText("💥 Bạn đã thua! Trò chơi kết thúc.");
            revealAllMines();
        } else {
            int neighboringMines = countNeighboringMines(row, col);
            cells[row][col].setText(neighboringMines == 0 ? "" : String.valueOf(neighboringMines));
            cells[row][col].setEnabled(false);

            if (neighboringMines == 0) {
                revealSurroundingCells(row, col);
            }

            if (checkWin()) {
                timer.stop();
                long score = Math.max(1000 - elapsedTime * 5, 0);
                statusLabel.setText("🎉 Bạn đã thắng! Chúc mừng! ⏱ " + elapsedTime + " giây | 🏆 Điểm: " + score);
            }
        }
    }

    private void toggleFlag(int row, int col) {
        if (revealed[row][col]) return;

        flagged[row][col] = !flagged[row][col];
        cells[row][col].setText(flagged[row][col] ? "🚩" : "");
    }

    private int countNeighboringMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && mines[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean checkWin() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (!revealed[row][col] && !mines[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void revealAllMines() {
        if (timer != null) {
            timer.stop(); // ⛔ Dừng đồng hồ khi thua
        }
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (mines[row][col]) {
                    cells[row][col].setText("💣");
                    cells[row][col].setEnabled(false);
                }
            }
        }
    }

    private void revealSurroundingCells(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (r >= 0 && r < SIZE && c >= 0 && c < SIZE && !revealed[r][c]) {
                    handleMove(r, c);
                }
            }
        }
    }
    
    private void updateTimer() {
        elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        statusLabel.setText("⏱ Thời gian: " + elapsedTime + " giây");
    }
}

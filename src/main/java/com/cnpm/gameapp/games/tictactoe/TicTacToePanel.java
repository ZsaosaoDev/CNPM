package com.cnpm.gameapp.games.tictactoe;

import com.cnpm.gameapp.core.GamePanel;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;

public class TicTacToePanel implements GamePanel {
    private static final int SIZE = 20;
    private final JPanel panel = new JPanel(new BorderLayout());
    private final JButton[][] cells = new JButton[SIZE][SIZE];
    private final TicTacToeLogic logic;
    private final TicTacToeAI ai;
    private JLabel statusLabel;
    private JLabel aiThinkingLabel;
    private boolean isAgainstComputer = false;

    public TicTacToePanel(CardLayout layout, JPanel container) {
        // 1.1.3 new TicTacToeLogic()
        logic = new TicTacToeLogic(cells);

        // 1.1.4 new TicTacToeAI(cells, logic)
        ai = new TicTacToeAI(cells, logic);

        // 1.1.5 showModeSelectionScreen()
        showModeSelectionScreen(layout, container);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void resetGame() {
        logic.resetBoard();
        statusLabel.setText("Lượt: ❌");
        aiThinkingLabel.setText("");
    }

    private void showModeSelectionScreen(CardLayout layout, JPanel container) {
        JPanel modeSelectionPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎮 CHỌN CHẾ ĐỘ CHƠI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        modeSelectionPanel.add(titleLabel, BorderLayout.NORTH);

        JButton pvpButton = new JButton("PvP (Người chơi vs Người chơi)");
        pvpButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pvpButton.addActionListener(e -> {
            isAgainstComputer = false;

            // 1.1.7 startGame(layout, container)
            startGame(layout, container);
        });

        JButton pvcButton = new JButton("PvC (Người chơi vs Máy)");
        pvcButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        pvcButton.addActionListener(e -> {
            isAgainstComputer = true;

            // 1.1.7 startGame(layout, container)
            startGame(layout, container);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pvpButton);
        buttonPanel.add(pvcButton);
        modeSelectionPanel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(modeSelectionPanel, BorderLayout.CENTER);
    }

    private void startGame(CardLayout layout, JPanel container) {
        panel.removeAll();
        logic.setXTurn(true);

        statusLabel = new JLabel("Lượt: ❌", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        aiThinkingLabel = new JLabel("", SwingConstants.CENTER);
        aiThinkingLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(statusLabel, BorderLayout.NORTH);
        northPanel.add(aiThinkingLabel, BorderLayout.SOUTH);
        panel.add(northPanel, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(SIZE, SIZE));
        int cellSize = 35;
        Font cellFont = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(cellSize, cellSize));
                btn.setFont(cellFont);
                final int r = row, c = col;

                // 1.1.8 Click cell (handleMove(row, col))
                btn.addActionListener(e -> handleMove(r, c));
                cells[row][col] = btn;
                board.add(btn);
            }
        }
        panel.add(board, BorderLayout.CENTER);

        JButton backButton = new JButton("⬅ Quay lại menu");
        backButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        backButton.addActionListener(e -> layout.show(container, "menu"));

        JButton changeModeButton = new JButton("🔄 Chọn lại chế độ");
        changeModeButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        changeModeButton.addActionListener(e -> {
            panel.removeAll();
            showModeSelectionScreen(layout, container);
            panel.revalidate();
            panel.repaint();
        });

        JPanel southPanel = new JPanel();
        southPanel.add(backButton);
        southPanel.add(changeModeButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void handleMove(int row, int col) {
        // 1.1.9 isValidMove(row, col)
        if (!logic.isValidMove(row, col)) return;

        String mark = logic.isXTurn() ? "❌" : "⭕";
        cells[row][col].setText(mark);

        // 1.1.11 checkWin(row, col, mark)
        if (logic.checkWin(row, col, mark)) {
            JOptionPane.showMessageDialog(panel, "🎉 " + mark + " thắng!");  // 1.1.20 showMessageDialog(Win)
            resetGame();
        } else if (logic.isBoardFull()) {
            JOptionPane.showMessageDialog(panel, "🏁 Hòa!");  // 1.1.20 showMessageDialog(Draw)
            resetGame();
        } else {
            logic.toggleTurn();
            statusLabel.setText("Lượt: " + (logic.isXTurn() ? "❌" : "⭕"));

            if (isAgainstComputer && !logic.isXTurn()) {
                // 1.1.13 AI Turn: computerMove(callback)
                logic.setBoardEnabled(false);
                aiThinkingLabel.setText("🤖 AI đang suy nghĩ...");
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        ai.computerMove(TicTacToePanel.this::updateAfterAIMove);  // 1.1.15
                        return null;
                    }

                    @Override
                    protected void done() {
                        logic.setBoardEnabled(true);
                        aiThinkingLabel.setText("");
                    }
                }.execute();
            }
        }
    }

    private void updateAfterAIMove(int row, int col) {
        // 1.1.18 updateAfterAIMove(row, col)
        cells[row][col].setText("⭕");

        // 1.1.19 checkWin(row, col, "⭕")
        if (logic.checkWin(row, col, "⭕")) {
            JOptionPane.showMessageDialog(panel, "🎉 Máy thắng!");  // 1.1.20 showMessageDialog(Win)
            resetGame();
        } else if (logic.isBoardFull()) {
            JOptionPane.showMessageDialog(panel, "🏁 Hòa!");  // 1.1.20 showMessageDialog(Draw)
            resetGame();
        } else {
            logic.setXTurn(true);
            statusLabel.setText("Lượt: ❌");
        }
    }
}

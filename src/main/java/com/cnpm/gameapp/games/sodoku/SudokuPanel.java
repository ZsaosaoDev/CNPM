package com.cnpm.gameapp.games.sodoku;

import com.cnpm.gameapp.core.GamePanel;
import com.cnpm.gameapp.core.NavigationButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuPanel implements GamePanel {
	private static final int DEFAULT_FONT_SIZE = 16;
	private final JPanel panel = new JPanel(new BorderLayout());
	private final SudokuLogic logic;
	private final SudokuTimer timer;
	private JButton[][] cells;
	private int currentlySelectedRow = -1;
	private int currentlySelectedCol = -1;
	private JPanel buttonSelectionPanel;
	private JLabel statusLabel;
	private final CardLayout layout;
	private final JPanel container;
	private SudokuPuzzleType currentType;

	public SudokuPanel(CardLayout layout, JPanel container) {
		this.layout = layout;
		this.container = container;
		this.logic = new SudokuLogic();
		this.timer = new SudokuTimer(this::updateStatusLabel);
		showModeSelectionScreen();
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void resetGame() {
		startGame(currentType, DEFAULT_FONT_SIZE);
	}

	private void showModeSelectionScreen() {
		panel.removeAll();
		JPanel modePanel = new JPanel(new BorderLayout());
		JLabel title = new JLabel("🎮 CHỌN CHẾ ĐỘ SUDOKU", SwingConstants.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 22));
		modePanel.add(title, BorderLayout.NORTH);

		JPanel btnPanel = new JPanel();

		// Fix: Separate button creation and listener assignment
		JButton easyButton = NavigationButton.create("6x6 (Dễ)", null, layout, container);
		easyButton.addActionListener(e -> startGame(SudokuPuzzleType.SIXBYSIX, DEFAULT_FONT_SIZE));
		btnPanel.add(easyButton);

		JButton normalButton = NavigationButton.create("9x9 (Thường)", null, layout, container);
		normalButton.addActionListener(e -> startGame(SudokuPuzzleType.NINEBYNINE, DEFAULT_FONT_SIZE));
		btnPanel.add(normalButton);

		JButton hardButton = NavigationButton.create("12x12 (Khó)", null, layout, container);
		hardButton.addActionListener(e -> startGame(SudokuPuzzleType.TWELVEBYTWELVE, DEFAULT_FONT_SIZE));
		btnPanel.add(hardButton);

		modePanel.add(btnPanel, BorderLayout.CENTER);

		modePanel.add(NavigationButton.create("⬅ Quay lại menu", "menu", layout, container), BorderLayout.SOUTH);

		panel.add(modePanel, BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
	}

	private void startGame(SudokuPuzzleType type, int fontSize) {
		panel.removeAll();
		this.currentType = type;
		logic.initializeGame(type);

		// Status panel
		statusLabel = new JLabel("Chọn ô để nhập số", SwingConstants.CENTER);
		statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(statusLabel, BorderLayout.NORTH);
		panel.add(northPanel, BorderLayout.NORTH);

		// Sudoku grid
		int size = type.getRows();
		cells = new JButton[size][size];
		JPanel board = new JPanel(new GridLayout(size, size, 1, 1));
		board.setBackground(Color.BLACK);
		Font cellFont = new Font("SansSerif", Font.BOLD, fontSize);

		// Calculate cell size based on board size
		int cellSize = calculateCellSize(size);
		int adjustedFontSize = calculateFontSize(size, fontSize);
		cellFont = new Font("SansSerif", Font.BOLD, adjustedFontSize);

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				JButton btn = new JButton();
				btn.setPreferredSize(new Dimension(cellSize, cellSize));
				btn.setFont(cellFont);
				btn.setBackground(Color.WHITE);
				btn.setOpaque(true);
				btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

				String value = logic.getValue(row, col);
				if (!value.isEmpty()) {
					btn.setText(value);
					btn.setEnabled(false);
					btn.setBackground(new Color(220, 220, 220));
				}

				final int r = row, c = col;
				btn.addActionListener(e -> handleCellClick(r, c));
				cells[row][col] = btn;
				board.add(btn);
			}
		}
		panel.add(board, BorderLayout.CENTER);

		// Start timer after statusLabel is initialized
		timer.start();

		// Number selection panel
		JPanel southPanel = new JPanel(new BorderLayout(10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel numberTitle = new JLabel("Chọn số:", SwingConstants.CENTER);
		numberTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
		southPanel.add(numberTitle, BorderLayout.NORTH);

		buttonSelectionPanel = new JPanel();
		int numColumns = logic.getValidValues().length;
		buttonSelectionPanel.setLayout(new GridLayout(0, numColumns, 5, 5));

		// Calculate size for number selection buttons
		int buttonSize = Math.max(30, 50 - (size - 6) * 3);
		int buttonFontSize = Math.max(12, 16 - (size - 6));

		for (String value : logic.getValidValues()) {
			JButton b = new JButton(value);
			b.setFont(new Font("SansSerif", Font.BOLD, buttonFontSize));
			b.setPreferredSize(new Dimension(buttonSize, buttonSize));
			b.setMargin(new Insets(5, 5, 5, 5));
			b.addActionListener(new NumActionListener());
			buttonSelectionPanel.add(b);
		}

		JButton clearButton = new JButton("Xóa");
		clearButton.setFont(new Font("SansSerif", Font.BOLD, buttonFontSize));
		clearButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
		clearButton.setMargin(new Insets(5, 5, 5, 5));
		clearButton.setBackground(new Color(255, 200, 200));
		clearButton.addActionListener(e -> handleClear());
		buttonSelectionPanel.add(clearButton);

		southPanel.add(buttonSelectionPanel, BorderLayout.CENTER);

		// Control panel
		JPanel controlPanel = new JPanel(new GridLayout(1, 4, 5, 5));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// Fix: Separate button creation and listener assignment
		JButton restartButton = NavigationButton.create("🔄 Chơi lại", null, layout, container);
		restartButton.addActionListener(e -> startGame(type, fontSize));
		controlPanel.add(restartButton);

		JButton modeButton = NavigationButton.create("Chọn lại chế độ", null, layout, container);
		modeButton.addActionListener(e -> showModeSelectionScreen());
		controlPanel.add(modeButton);

		JButton checkButton = NavigationButton.create("✅ Kiểm tra", null, layout, container);
		checkButton.addActionListener(e -> checkSolution());
		controlPanel.add(checkButton);

		controlPanel.add(NavigationButton.create("⬅ Quay lại menu", "menu", layout, container));

		southPanel.add(controlPanel, BorderLayout.SOUTH);
		panel.add(southPanel, BorderLayout.SOUTH);

		panel.revalidate();
		panel.repaint();
	}

	private void handleCellClick(int row, int col) {
		if (!logic.isSlotMutable(row, col))
			return;

		if (currentlySelectedRow != -1 && currentlySelectedCol != -1) {
			cells[currentlySelectedRow][currentlySelectedCol].setBackground(Color.WHITE);
		}

		currentlySelectedRow = row;
		currentlySelectedCol = col;
		cells[row][col].setBackground(new Color(173, 216, 230));
	}

	private void handleClear() {
		if (currentlySelectedRow != -1 && currentlySelectedCol != -1
				&& logic.isSlotMutable(currentlySelectedRow, currentlySelectedCol)) {
			logic.makeMove(currentlySelectedRow, currentlySelectedCol, "");
			cells[currentlySelectedRow][currentlySelectedCol].setText("");
		}
	}

	private void checkSolution() {
		if (!logic.isBoardFull()) {
			JOptionPane.showMessageDialog(panel, "Vui lòng điền đầy đủ tất cả các ô!", "Lỗi",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		boolean isCorrect = logic.isValidSolution();
		resetCellColors();

		if (isCorrect) {
			timer.stop();
			long score = timer.getScore();
			JOptionPane.showMessageDialog(panel, "Chúc mừng! Bạn đã giải đúng bài Sudoku!\n⏱ Thời gian: "
							+ timer.getElapsedTime() + " giây | 🏆 Điểm: " + score, "Thành công",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			highlightIncorrectCells();
			JOptionPane.showMessageDialog(panel, "Lời giải chưa đúng! Các ô sai đã được tô đỏ.", "Sai",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void resetCellColors() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col].setBackground(logic.isSlotMutable(row, col) ? Color.WHITE : new Color(220, 220, 220));
			}
		}
	}

	private void highlightIncorrectCells() {
		for (int row = 0; row < cells.length; row++) {
			for (int col = 0; col < cells[row].length; col++) {
				String value = logic.getValue(row, col);
				if (!value.isEmpty() && !logic.isValidMove(row, col, value)) {
					cells[row][col].setBackground(new Color(255, 182, 193));
				}
			}
		}
	}

	private void updateStatusLabel(String message) {
		statusLabel.setText(message);
	}

	/**
	 * Calculate cell size based on board size
	 *
	 * @param boardSize Size of the board (6, 9, 12)
	 * @return Appropriate cell size
	 */
	private int calculateCellSize(int boardSize) {
		// Cell size decreases as board size increases
		switch (boardSize) {
			case 6:
				return 60; // 6x6 - large cells
			case 9:
				return 50; // 9x9 - medium cells
			case 12:
				return 40; // 12x12 - small cells
			default:
				return 50;
		}
	}

	/**
	 * Calculate font size based on board size
	 *
	 * @param boardSize       Size of the board
	 * @param defaultFontSize Default font size
	 * @return Appropriate font size
	 */
	private int calculateFontSize(int boardSize, int defaultFontSize) {
		// Font size decreases as board size increases
		switch (boardSize) {
			case 6:
				return defaultFontSize + 2; // 6x6 - larger font
			case 9:
				return defaultFontSize; // 9x9 - default font
			case 12:
				return defaultFontSize - 2; // 12x12 - smaller font
			default:
				return defaultFontSize;
		}
	}

	private class NumActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String value = ((JButton) e.getSource()).getText();
			if (currentlySelectedRow != -1 && currentlySelectedCol != -1
					&& logic.isSlotMutable(currentlySelectedRow, currentlySelectedCol)) {
				if (logic.makeMove(currentlySelectedRow, currentlySelectedCol, value)) {
					cells[currentlySelectedRow][currentlySelectedCol].setText(value);
				}
			}
		}
	}
}
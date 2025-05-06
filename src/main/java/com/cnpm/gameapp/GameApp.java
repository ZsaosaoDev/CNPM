package com.cnpm.gameapp;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;
import com.cnpm.gameapp.games.tictactoe.TicTacToePanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class GameApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(GameApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("🎮 Mini Game Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);

        // Trang menu chính
        JPanel mainMenu = createMainMenuPanel(container, cardLayout);
        container.add(mainMenu, "menu");

        // Thêm các game thực sự vào container
        container.add(createTicTacToePanel(cardLayout, container), "tictactoe");
        // Thêm game giả lập khác nếu cần
        container.add(createDummyGamePanel("Rắn săn mồi", cardLayout, container), "snake");
        container.add(createDummyGamePanel("Dò mìn", cardLayout, container), "minesweeper");
        container.add(createDummyGamePanel("Ghép thẻ", cardLayout, container), "memory");

        frame.setContentPane(container);
        frame.setVisible(true);
    }

    private static JPanel createMainMenuPanel(JPanel container, CardLayout layout) {
        JPanel panel = new JPanel(new MigLayout("wrap 1, align center", "[grow,center]", "[]20[]10[]10[]10[]"));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("🎮 CHỌN MỘT TRÒ CHƠI");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, "growx");

        panel.add(createNavButton("Cờ Caro", "tictactoe", layout, container), "growx");
        panel.add(createNavButton("Rắn săn mồi", "snake", layout, container), "growx");
        panel.add(createNavButton("Dò mìn", "minesweeper", layout, container), "growx");
        panel.add(createNavButton("Ghép thẻ", "memory", layout, container), "growx");

        return panel;
    }

    private static JButton createNavButton(String text, String targetCard, CardLayout layout, JPanel container) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        button.setPreferredSize(new Dimension(200, 40));

        button.addActionListener(e -> layout.show(container, targetCard));
        return button;
    }

    private static JPanel createTicTacToePanel(CardLayout layout, JPanel container) {
        // Tạo panel Cờ Caro thực sự
        return new TicTacToePanel(layout, container);
    }

    private static JPanel createDummyGamePanel(String gameName, CardLayout layout, JPanel container) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("👾 Giao diện " + gameName, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 24));

        JButton backButton = new JButton("⬅ Quay lại menu");
        backButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener(e -> layout.show(container, "menu"));

        panel.add(label, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }
}

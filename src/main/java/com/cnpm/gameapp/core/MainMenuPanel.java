package com.cnpm.gameapp.core;


import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(JPanel container, CardLayout layout) {
        setLayout(new MigLayout("wrap 1, align center", "[grow,center]", "[]20[]10[]10[]10[]10[]"));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("🎮 CHỌN MỘT TRÒ CHƠI");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, "growx");

//      1.1.1 click("Cờ caro")
        add(NavigationButton.create("Cờ Caro", "tictactoe", layout, container), "growx");
        add(NavigationButton.create("Dò mìn", "minesweeper", layout, container), "growx");
        add(NavigationButton.create("Sudoku", "sudoku", layout, container), "growx");
        add(NavigationButton.create("Ghép thẻ", "memory", layout, container), "growx");
    }
}

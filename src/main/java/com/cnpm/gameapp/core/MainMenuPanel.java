package com.cnpm.gameapp.core;


import com.cnpm.gameapp.system.IGameExit;
import com.cnpm.gameapp.system.LaucherManager;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        add(NavigationButton.create("Trò chơi thẻ bài", "memory", layout, container, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LaucherManager.getInstance().launchGame();
                GameApp.getFrame().setVisible(false);
            }
        }), "growx");

        LaucherManager.getInstance().addGameExitListener(new IGameExit() {
            @Override
            public void onGameExit(int exitCode) {
                GameApp.getFrame().setVisible(true);
            }
        });
    }
}

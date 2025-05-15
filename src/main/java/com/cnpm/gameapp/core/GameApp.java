package com.cnpm.gameapp.core;

import com.cnpm.gameapp.games.minesweeper.MinesweeperPanel;
import com.cnpm.gameapp.games.sodoku.SudokuPanel;
import com.cnpm.gameapp.games.tictactoe.TicTacToePanel;
import com.formdev.flatlaf.FlatLightLaf;

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
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);


        container.add(new MainMenuPanel(container, cardLayout), "menu");

        //1.1.2 new TicTacToePanel()
        container.add(new TicTacToePanel(cardLayout, container).getPanel(), "tictactoe");
        container.add(new MinesweeperPanel(cardLayout, container).getPanel(), "minesweeper");
        container.add(new SudokuPanel(cardLayout, container).getPanel(), "sudoku");
//        container.add(new MemoryGamePanel(cardLayout, container).getPanel(), "memory");

        frame.setContentPane(container);
        frame.setVisible(true);
    }
}

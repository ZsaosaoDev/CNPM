package com.cnpm.gameapp.core;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NavigationButton {
    public static JButton create(String text, String targetCard, CardLayout layout, JPanel container, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
        button.setPreferredSize(new Dimension(200, 40));
        if (targetCard != null) {
            button.addActionListener(e -> layout.show(container, targetCard));
        }
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    public static JButton create(String text, String targetCard, CardLayout layout, JPanel container) {
        return create(text, targetCard, layout, container, null);
    }
}
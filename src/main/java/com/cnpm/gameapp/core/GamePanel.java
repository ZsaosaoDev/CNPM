package com.cnpm.gameapp.core;

import javax.swing.*;
import java.awt.*;

public interface GamePanel {
    JPanel getPanel(); // Trả về panel của game
    void resetGame();  // Đặt lại trạng thái game
}

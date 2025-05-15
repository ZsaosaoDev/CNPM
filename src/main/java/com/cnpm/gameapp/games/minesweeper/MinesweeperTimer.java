package com.cnpm.gameapp.games.minesweeper;

import javax.swing.*;
import java.util.function.Consumer;

public class MinesweeperTimer {
    private Timer timer;
    private long startTime;
    private long elapsedTime;
    private int minesCount = 10;
    private final Consumer<String> statusUpdater;

    public MinesweeperTimer(Consumer<String> statusUpdater) {
        this.statusUpdater = statusUpdater;
    }

    public void setMinesCount(int minesCount) {
        this.minesCount = minesCount;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timer = new Timer(1000, e -> updateTimer());
        timer.start();
        updateTimer();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void reset() {
        stop();
        elapsedTime = 0;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public long getScore() {
        return Math.max(1000 - elapsedTime * 5, 0);
    }

    private void updateTimer() {
        elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        statusUpdater.accept("⏱ Thời gian: " + elapsedTime + " giây");
    }
}
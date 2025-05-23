package com.cnpm.gameapp.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class LaucherManager {
//    private static final String GAME_EXE = "E:/NOIR_NOAH_EN_R18/r18NOIRNOAH_EN/Game.exe";
    private static final String PROJECT_HOME = "game/the-card-game";
    private List<IGameExit> gameExitListeners;
    private static LaucherManager instance;

    private LaucherManager() {
        gameExitListeners = new ArrayList<>();
    }
    
    public static LaucherManager getInstance() {
        if (instance == null) {
            instance = new LaucherManager();
        }
        return instance;
    }

    public void addGameExitListener(IGameExit listener) {
        gameExitListeners.add(listener);
    }

    public void removeGameExitListener(IGameExit listener) {
        gameExitListeners.remove(listener);
    }

    public void notifyGameExit(int exitCode) {
        for (IGameExit listener : gameExitListeners) {
            listener.onGameExit(exitCode);
        }
    }

    public void launchGame() {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "cmd", "/c run.bat"
            );
            processBuilder.directory(new File(PROJECT_HOME));
            System.out.println("Game start");
            Process process = processBuilder.start();
            new Thread(() -> {
                try {

                    int exitCode = process.waitFor();
                    System.out.println("Game stopped with exit code: " + exitCode);
                    notifyGameExit(exitCode);
                } catch (InterruptedException e) {
                    notifyGameExit(-1); // Notify with error code if interrupted
                    System.err.println("Game process interrupted: " + e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Failed to launch game: " + e.getMessage());
        }
    }
}


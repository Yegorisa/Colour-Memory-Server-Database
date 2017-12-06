package com.egoregorov.colourmemory.view;

/**
 * Created by Egor on 04.12.2017.
 */

public interface IBoardView {
    void startNewGame();
    void lostScore(int position1, int position2, int currentScore);
    void gotTheScore(int position1, int position2, int currentScore);
    void gameCompleted(int finalScore);
}

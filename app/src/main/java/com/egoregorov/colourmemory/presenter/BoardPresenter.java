package com.egoregorov.colourmemory.presenter;

import android.os.AsyncTask;

import com.egoregorov.colourmemory.model.Board;
import com.egoregorov.colourmemory.model.Card;
import com.egoregorov.colourmemory.view.BoardView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Egor on 04.12.2017.
 */

public class BoardPresenter implements Presenter {
    private static final String TAG = "BoardPresenter";

    private Board mModel;
    private BoardView mBoardView;
    private int mScore;
    private Card mPreviousCard;
    private Card mCurrentCard;

    public BoardPresenter(BoardView boardView) {
        mBoardView = boardView;
        mModel = new Board();
        mScore = 0;
    }

    @Override
    public void onCreate() {
        mModel = new Board();
        mBoardView.startNewGame();
        mScore = 0;
    }


    public Card onCardSelected(int position) {
        if (mModel.getCard(position).isSelected()) {
            return null;
        } else {
            Card selectedCard = mModel.getCard(position);
            if (mPreviousCard != null) {
                mCurrentCard = selectedCard;

                if (selectedCard.getImageResourceId() == mPreviousCard.getImageResourceId()) {
                    new DismissCardsTask().execute(true);
                } else {
                    mCurrentCard.setSelected(false);
                    mPreviousCard.setSelected(false);
                    new DismissCardsTask().execute(false);

                }
            } else {
                selectedCard.setSelected(true);
                mPreviousCard = selectedCard;
            }
            return selectedCard;
        }
    }

    private void gotTheScore(){
        mScore = mScore + 2;
        mModel.minusTwoCards();
        mBoardView.gotTheScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }
    private void lostScore(){
        mScore = mScore - 1;
        mBoardView.lostScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }
    private void gameFinished(){
        mBoardView.gameCompleted(mScore);
        BoardPresenter.this.onCreate();
    }

    class DismissCardsTask extends AsyncTask<Boolean, Void, Void> {
        boolean mWasRight;

        @Override
        protected Void doInBackground(Boolean... booleans) {
            try {
                TimeUnit.MILLISECONDS.sleep(750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mWasRight = booleans[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mWasRight) {
                gotTheScore();
            } else {
                lostScore();
            }
            if (mModel.getCardsLeft() == 0) {
                gameFinished();
            }
            mCurrentCard = null;
            mPreviousCard = null;
        }
    }

}
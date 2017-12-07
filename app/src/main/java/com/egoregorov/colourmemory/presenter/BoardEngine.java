package com.egoregorov.colourmemory.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.egoregorov.colourmemory.database.DatabaseMethods;
import com.egoregorov.colourmemory.database.Record;
import com.egoregorov.colourmemory.model.Board;
import com.egoregorov.colourmemory.model.Card;
import com.egoregorov.colourmemory.services.IPostResponse;
import com.egoregorov.colourmemory.services.NetworkService;
import com.egoregorov.colourmemory.view.IBoardView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BoardEngine implements IPresenter, IPostResponse {

    private Board mModel;
    private IBoardView mBoardView;
    private int mScore;
    private Card mPreviousCard;
    private Card mCurrentCard;
    private DismissCardsTask mDismissCardsTask;

    private Context mContext;

    private List<Record> mUnsavedRecords = new LinkedList<>();

    public BoardEngine(IBoardView boardView, Context context) {
        mBoardView = boardView;
        mContext = context;
    }

    @Override
    public void onCreate() {
        mModel = new Board().shuffle();
        mBoardView.startNewGame();
        mScore = 0;
        mPreviousCard = null;
        mCurrentCard = null;
        if (mDismissCardsTask != null) {
            mDismissCardsTask.cancel(true);
        }
    }

    @Override
    public void successfulPost() {
        ((Activity)mContext).runOnUiThread(() -> {
            DatabaseMethods.updateRecordToSavedOnServer(mUnsavedRecords.get(0));
            mUnsavedRecords.remove(0);
            if (mUnsavedRecords.size() != 0) {
                uploadUnsavedRecord();
            }
        });
    }

    @Override
    public void error() {
    }

    public Card onCardSelected(int position) {
        if (mModel.getCard(position).isSelected()) {
            return null;
        } else {
            Card selectedCard = mModel.getCard(position);
            if (mPreviousCard != null) {
                mCurrentCard = selectedCard;

                if (selectedCard.getImageResourceId() == mPreviousCard.getImageResourceId()) {
                    mDismissCardsTask = new DismissCardsTask(true);
                    mDismissCardsTask.execute();
                } else {
                    mCurrentCard.setSelected(false);
                    mPreviousCard.setSelected(false);
                    mDismissCardsTask = new DismissCardsTask(false);
                    mDismissCardsTask.execute();

                }
            } else {
                selectedCard.setSelected(true);
                mPreviousCard = selectedCard;
            }
            return selectedCard;
        }
    }

    public void checkIfAllRecordsAreUpToDate() {
        ArrayList<Record> recordArrayList = DatabaseMethods.getAllRecords();
        for (Record record :
                recordArrayList) {
            if (!record.isSavedOnServer()) {
                mUnsavedRecords.add(record);
            }
        }

        if (mUnsavedRecords.size() != 0) {
            uploadUnsavedRecord();
        }
    }

    private void uploadUnsavedRecord() {
        NetworkService networkService = new NetworkService();
        networkService.postRecord(BoardEngine.this, mUnsavedRecords.get(0));
    }

    private void gotTheScore() {
        mScore = mScore + 2;
        mModel.removeTwoCards();
        mBoardView.gotTheScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }

    private void lostScore() {
        mScore = mScore - 1;
        mBoardView.lostScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }

    private void gameFinished() {
        mBoardView.gameCompleted(mScore);
        BoardEngine.this.onCreate();
    }

    class DismissCardsTask extends AsyncTask<Void, Void, Void> {
        boolean mWasRight;

        DismissCardsTask(boolean wasRight) {
            mWasRight = wasRight;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            if (mModel.cardsLeft() == 0) {
                gameFinished();
            }
            mCurrentCard = null;
            mPreviousCard = null;
        }
    }

}
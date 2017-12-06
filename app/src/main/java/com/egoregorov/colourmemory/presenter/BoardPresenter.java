package com.egoregorov.colourmemory.presenter;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Egor on 04.12.2017.
 */

public class BoardPresenter implements IPresenter, IPostResponse {
    private static final String TAG = "BoardPresenter";

    private Board mModel;
    private IBoardView mBoardView;
    private int mScore;
    private Card mPreviousCard;
    private Card mCurrentCard;
    private DismissCardsTask mDismissCardsTask;

    private List<Record> mUnsavedRecords = new LinkedList<>();

    public BoardPresenter(IBoardView boardView) {
        mBoardView = boardView;
        mModel = new Board();
        mScore = 0;
    }

    @Override
    public void onCreate() {
        mModel = new Board();
        mBoardView.startNewGame();
        mScore = 0;
        mPreviousCard = null;
        mCurrentCard = null;
        if (mDismissCardsTask != null){
            mDismissCardsTask.cancel(true);
        }
    }

    @Override
    public void successfulPost() {
        DatabaseMethods.updateRecordToSavedOnServer(mUnsavedRecords.get(0));
        mUnsavedRecords.remove(0);
        Log.d(TAG, "successfulPost: succesfully updated");
        if (mUnsavedRecords.size() != 0) {
            uploadUnsavedRecord();
        }
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
                    mDismissCardsTask = new DismissCardsTask();
                    mDismissCardsTask.execute(true);
                } else {
                    mCurrentCard.setSelected(false);
                    mPreviousCard.setSelected(false);
                    mDismissCardsTask = new DismissCardsTask();
                    mDismissCardsTask.execute(false);

                }
            } else {
                selectedCard.setSelected(true);
                mPreviousCard = selectedCard;
            }
            return selectedCard;
        }
    }

    public void checkIfAllRecordsAreUpToDate() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Record> recordArrayList = DatabaseMethods.getAllRecords();
                for (int i = 0; i < 10; i++) {
                    Record record = recordArrayList.get(i);
                    if (!record.isSavedOnServer()) {
                        mUnsavedRecords.add(record);
                    }
                }
                if (mUnsavedRecords.size() != 0) {
                    uploadUnsavedRecord();
                }
            }
        });
    }

    private void uploadUnsavedRecord() {
        NetworkService networkService = new NetworkService();
        networkService.postRecord(BoardPresenter.this, mUnsavedRecords.get(0));
    }

    private void gotTheScore() {
        mScore = mScore + 2;
        mModel.minusTwoCards();
        mBoardView.gotTheScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }

    private void lostScore() {
        mScore = mScore - 1;
        mBoardView.lostScore(mModel.getCardPosition(mPreviousCard), mModel.getCardPosition(mCurrentCard), mScore);
    }

    private void gameFinished() {
        mBoardView.gameCompleted(mScore);
        BoardPresenter.this.onCreate();
    }

    class DismissCardsTask extends AsyncTask<Boolean, Void, Void> {
        boolean mWasRight;

        @Override
        protected Void doInBackground(Boolean... booleans) {
            try {
                TimeUnit.MILLISECONDS.sleep(1250);
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
package com.egoregorov.colourmemory.database;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Record extends RealmObject implements Comparable<Record> {

    @PrimaryKey
    private String mUserId;

    private String mUserName;
    private int mScore;
    private boolean mSavedOnServer = false;

    public Record() {
    }

    public Record(String userName, int score) {
        mUserName = userName;
        mScore = score;
        mUserId = UUID.randomUUID().toString();
    }

    public Record(String userId, String userName, int score) {
        mUserId = userId;
        mUserName = userName;
        mScore = score;
    }

    public String getUserId() {
        return mUserId;
    }

    public boolean isSavedOnServer() {
        return mSavedOnServer;
    }

    public void setSavedOnServer(boolean savedOnServer) {
        mSavedOnServer = savedOnServer;
    }

    public int getScore() {
        return mScore;
    }

    public String getUserName() {
        return mUserName;
    }

    @Override
    public int compareTo(@NonNull Record record) {
        if (mScore < record.getScore()) {
            return 1;
        } else if (mScore > record.getScore()) {
            return -1;
        } else return 0;
    }
}

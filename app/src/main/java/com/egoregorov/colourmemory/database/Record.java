package com.egoregorov.colourmemory.database;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Egor on 05.12.2017.
 */

public class Record extends RealmObject implements Comparable<Record> {
    @PrimaryKey
    private String mUserId;

    private String mUserName;
    private int mRank;
    private int mScore;

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

    public int getRank() {
        return mRank;
    }

    public void setRank(int rank) {
        mRank = rank;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
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

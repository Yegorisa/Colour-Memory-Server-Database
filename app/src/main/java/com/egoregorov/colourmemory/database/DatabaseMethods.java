package com.egoregorov.colourmemory.database;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Egor on 05.12.2017.
 */

public class DatabaseMethods {
    private static final String TAG = "DatabaseMethods";

    public static ArrayList<Record> getAllRecords() {
        Realm myRealm = Realm.getDefaultInstance();
        final RealmResults<Record> allRecords = myRealm.where(Record.class).findAll();
        ArrayList<Record> recordsArrayList = new ArrayList<>();
        recordsArrayList.addAll(allRecords);
        Collections.sort(recordsArrayList);
        return recordsArrayList;
    }

    public static void saveRecord(Record recordToSave) {
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        myRealm.copyToRealm(recordToSave);
        myRealm.commitTransaction();
    }
}

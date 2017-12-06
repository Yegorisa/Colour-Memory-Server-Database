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

        if (recordsArrayList.size() > 10){
            ArrayList<Record> sortedList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                sortedList.add(recordsArrayList.get(i));
            }
            return sortedList;

        }
        return recordsArrayList;
    }

    public static void saveRecord(Record recordToSave) {
        Realm myRealm = Realm.getDefaultInstance();
        myRealm.beginTransaction();
        myRealm.copyToRealm(recordToSave);
        myRealm.commitTransaction();
    }

    public static void saveArrayOfRecords(ArrayList<Record> recordArrayList) {
        Realm myRealm = Realm.getDefaultInstance();
        final RealmResults<Record> allRecords = myRealm.where(Record.class).findAll();
        for (Record record :
                allRecords) {
            if (!record.isSavedOnServer()) {
                Record recordToSave = new Record(record.getUserId(), record.getUserName(), record.getScore());
                recordArrayList.add(recordToSave);
            }
        }
        myRealm.beginTransaction();
        myRealm.deleteAll();
        myRealm.copyToRealm(recordArrayList);
        myRealm.commitTransaction();
    }

    public static void updateRecordToSavedOnServer(Record record) {
        Realm myRealm = Realm.getDefaultInstance();
        final Record realmRecord = myRealm.where(Record.class).equalTo("mUserId", record.getUserId()).findFirst();
        if (realmRecord != null) {
            myRealm.beginTransaction();
            realmRecord.setSavedOnServer(true);
            myRealm.copyToRealmOrUpdate(realmRecord);
            myRealm.commitTransaction();
        }

    }
}

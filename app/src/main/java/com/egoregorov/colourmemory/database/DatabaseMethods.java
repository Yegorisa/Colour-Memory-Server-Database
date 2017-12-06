package com.egoregorov.colourmemory.database;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseMethods {

    public static ArrayList<Record> getAllRecords() {
        Realm myRealm = Realm.getDefaultInstance();
        final RealmResults<Record> allLocalDatabaseRecords = myRealm.where(Record.class).findAll();
        ArrayList<Record> recordsArrayList = new ArrayList<>();
        recordsArrayList.addAll(allLocalDatabaseRecords);
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

    public static void saveAllRecords(ArrayList<Record> newRecordArrayList) {
        Realm myRealm = Realm.getDefaultInstance();
        final RealmResults<Record> allLocalDatabaseRecords = myRealm.where(Record.class).findAll();
        for (Record record :
                allLocalDatabaseRecords) {
            if (!record.isSavedOnServer()) {
                Record recordToSave = new Record(record.getUserId(), record.getUserName(), record.getScore());
                newRecordArrayList.add(recordToSave);
            }
        }
        myRealm.beginTransaction();
        myRealm.deleteAll();
        myRealm.copyToRealm(newRecordArrayList);
        myRealm.commitTransaction();
    }

    public static void updateRecordToSavedOnServer(Record recordToUpdate) {
        Realm myRealm = Realm.getDefaultInstance();
        final Record realmRecord = myRealm.where(Record.class).equalTo("mUserId", recordToUpdate.getUserId()).findFirst();
        if (realmRecord != null) {
            myRealm.beginTransaction();
            realmRecord.setSavedOnServer(true);
            myRealm.copyToRealmOrUpdate(realmRecord);
            myRealm.commitTransaction();
        }
    }
}

package com.egoregorov.colourmemory.services;

import com.egoregorov.colourmemory.database.Record;

import java.util.ArrayList;

public interface IGetResponse {
    void gotTheResponse(ArrayList<Record> downloadedRecordsArrayList);
    void error();
}

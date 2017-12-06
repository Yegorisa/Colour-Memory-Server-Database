package com.egoregorov.colourmemory.services;

import com.egoregorov.colourmemory.database.Record;

import java.util.ArrayList;

/**
 * Created by Egor on 06.12.2017.
 */

public interface IGetResponse {
    void gotTheResponse(ArrayList<Record> downloadedRecordsArrayList);
    void error();
}

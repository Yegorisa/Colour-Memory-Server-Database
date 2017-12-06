package com.egoregorov.colourmemory.services;

import android.support.annotation.NonNull;

import com.egoregorov.colourmemory.database.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class NetworkService {
    private IGetResponse mIGetCallback;
    private IPostResponse mIPostCallback;

    public void getAllRecords(IGetResponse IGetCallback) {
        mIGetCallback = IGetCallback;
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("https://p-m-t.herokuapp.com/load-records")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                mIGetCallback.error();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    ArrayList<Record> recordArrayList = transformJsonDataToArray(responseBody.string());
                    if (recordArrayList != null) {
                        mIGetCallback.gotTheResponse(recordArrayList);
                    } else {
                        mIGetCallback.error();
                    }
                }
            }
        });
    }

    public void postRecord(IPostResponse iPostCallback, Record record) {
        mIPostCallback = iPostCallback;
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/json; charset=utf-8");

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        JSONObject jsonRecord = transformRecordToJsonObject(record);

        if (jsonRecord != null) {
            String postBody = jsonRecord.toString();
            Request request = new Request.Builder()
                    .url("https://p-m-t.herokuapp.com/save-record")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    mIPostCallback.error();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    mIPostCallback.successfulPost();
                }
            });
        } else {
            mIPostCallback.error();
        }

    }

    private ArrayList<Record> transformJsonDataToArray(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getString("message").equals("Success")) {
                ArrayList<Record> recordArrayList = new ArrayList<>();
                JSONArray jsonArrayOfRecords = jsonObject.getJSONArray("array");
                for (int i = 0; i < jsonArrayOfRecords.length(); i++) {
                    JSONObject recordJsonObject = jsonArrayOfRecords.getJSONObject(i);
                    String id = recordJsonObject.getString("_id");
                    String name = recordJsonObject.getString("name");
                    int score = recordJsonObject.getInt("score");
                    Record record = new Record(id, name, score);
                    recordArrayList.add(record);
                }
                Collections.sort(recordArrayList);
                return recordArrayList;
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONObject transformRecordToJsonObject(Record record) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", record.getUserName());
            jsonObject.put("score", record.getScore());
            return jsonObject;
        } catch (JSONException e) {
            return null;
        }
    }
}

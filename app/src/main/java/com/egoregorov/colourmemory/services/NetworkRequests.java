package com.egoregorov.colourmemory.services;

import android.util.Log;

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
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Egor on 06.12.2017.
 */

public class NetworkRequests {
    private static final String TAG = "NetworkRequests";
    private IGetResponse mCallback;

    public NetworkRequests(IGetResponse callback) {
        mCallback = callback;
    }

    public void getAllRecords() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url("https://p-m-t.herokuapp.com/load-records")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                mCallback.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    ArrayList<Record> recordArrayList = transformJsonDataToArray(responseBody.string());
                    if (recordArrayList != null){
                        mCallback.gotTheResponse(recordArrayList);
                    } else {
                        mCallback.error();
                    }
                }
            }

        });
    }
    private ArrayList<Record> transformJsonDataToArray(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getString("message").equals("Success")){
                ArrayList<Record> recordArrayList = new ArrayList<>();
                JSONArray jsonArrayOfRecords = jsonObject.getJSONArray("array");
                for (int i = 0; i <jsonArrayOfRecords.length() ; i++) {
                    JSONObject recordJsonObject = jsonArrayOfRecords.getJSONObject(i);
                    String id = recordJsonObject.getString("_id");
                    String name = recordJsonObject.getString("name");
                    int score = recordJsonObject.getInt("score");
                    Record record = new Record(id,name,score);
                    recordArrayList.add(record);
                }
                Collections.sort(recordArrayList);
                return recordArrayList;
            } else {
                return null;
            }
        }catch (JSONException e){
            Log.e(TAG, "transformJsonDataToArray: ERROR PARSING JSON DATA " + e.getMessage() );
        }
        return null;
    }
}

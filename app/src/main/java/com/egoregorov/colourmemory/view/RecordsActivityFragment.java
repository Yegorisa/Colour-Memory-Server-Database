package com.egoregorov.colourmemory.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.database.DatabaseMethods;
import com.egoregorov.colourmemory.database.Record;
import com.egoregorov.colourmemory.services.IGetResponse;
import com.egoregorov.colourmemory.services.NetworkService;

import java.util.ArrayList;

public class RecordsActivityFragment extends Fragment implements IGetResponse {
    private static final String SCREEN_LOADED = "SCREEN_LOADED";
    public static final String IS_DATA_OFFLINE = "IS_DATA_OFFLINE";

    private RecyclerView mRecyclerView;
    private boolean mScreenLoaded = false;
    private ProgressBar mProgressBar;
    private boolean mIsDataOffline;

    public RecordsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_records, container, false);

        mProgressBar = view.findViewById(R.id.fragment_records_loading);
        mRecyclerView = view.findViewById(R.id.fragment_records_topTenRecords);

        if (savedInstanceState != null) {
            mProgressBar.setVisibility(View.GONE);
            mScreenLoaded = savedInstanceState.getBoolean(SCREEN_LOADED);
            ArrayList<Record> records = DatabaseMethods.getAllRecords();
            TopTenRecordsAdapter recordsAdapter = new TopTenRecordsAdapter(records);
            mRecyclerView.setAdapter(recordsAdapter);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (!mScreenLoaded) {
            NetworkService networkService = new NetworkService();
            networkService.getAllRecords(this);
            mScreenLoaded = true;
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mIsDataOffline = savedInstanceState.getBoolean(IS_DATA_OFFLINE);
            if (mIsDataOffline) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle() + " " + getString(R.string.action_bar_offline));
            } else {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_highscores);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SCREEN_LOADED, mScreenLoaded);
        outState.putBoolean(IS_DATA_OFFLINE, mIsDataOffline);
    }

    @Override
    public void gotTheResponse(ArrayList<Record> downloadedRecordsArrayList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                mProgressBar.setVisibility(View.GONE);
                mIsDataOffline = false;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_highscores);
                for (Record record :
                        downloadedRecordsArrayList) {
                    record.setSavedOnServer(true);
                }
                DatabaseMethods.saveAllRecords(downloadedRecordsArrayList);
                ArrayList<Record> newArrayOfRecords = DatabaseMethods.getAllRecords();
                TopTenRecordsAdapter recordsAdapter = new TopTenRecordsAdapter(newArrayOfRecords);
                mRecyclerView.setAdapter(recordsAdapter);
            });
        }
    }

    @Override
    public void error() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                mIsDataOffline = true;
                mProgressBar.setVisibility(View.GONE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(((AppCompatActivity) getActivity()).getSupportActionBar().getTitle() + " " + getString(R.string.action_bar_offline));
                ArrayList<Record> records = DatabaseMethods.getAllRecords();
                TopTenRecordsAdapter recordsAdapter = new TopTenRecordsAdapter(records);
                mRecyclerView.setAdapter(recordsAdapter);
            });
        }

    }
}

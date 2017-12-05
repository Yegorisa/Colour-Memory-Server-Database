package com.egoregorov.colourmemory.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.database.DatabaseMethods;
import com.egoregorov.colourmemory.database.Record;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordsActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public RecordsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_records, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_records_topTenRecords);

        ArrayList<Record> records = DatabaseMethods.getAllRecords();
        TopTenRecordsAdapter recordsAdapter = new TopTenRecordsAdapter(records);
        mRecyclerView.setAdapter(recordsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }
}

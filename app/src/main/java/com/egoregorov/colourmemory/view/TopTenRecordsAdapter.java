package com.egoregorov.colourmemory.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.database.Record;

import java.util.ArrayList;

public class TopTenRecordsAdapter extends RecyclerView.Adapter<TopTenRecordsAdapter.RecordViewHolder> {

    private ArrayList<Record> mRecordArrayList;

    public TopTenRecordsAdapter(ArrayList<Record> recordArrayList) {
        mRecordArrayList = recordArrayList;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        Record record = mRecordArrayList.get(position);
        holder.userName.setText(record.getUserName());
        holder.score.setText(String.valueOf(record.getScore()));
        holder.rating.setText(String.valueOf(position + 1));
        switch (record.getScore()) {
            case 1:
                holder.rank.setImageResource(R.drawable.rank1);
                break;
            case 2:
                holder.rank.setImageResource(R.drawable.rank2);
                break;
            case 3:
                holder.rank.setImageResource(R.drawable.rank3);
                break;
            case 4:
                holder.rank.setImageResource(R.drawable.rank4);
                break;
            case 5:
                holder.rank.setImageResource(R.drawable.rank5);
                break;
            case 6:
                holder.rank.setImageResource(R.drawable.rank6);
                break;
            case 7:
                holder.rank.setImageResource(R.drawable.rank7);
                break;
            case 8:
                holder.rank.setImageResource(R.drawable.rank8);
                break;
            case 9:
                holder.rank.setImageResource(R.drawable.rank9);
                break;
            case 10:
                holder.rank.setImageResource(R.drawable.rank10);
                break;
            case 11:
                holder.rank.setImageResource(R.drawable.rank11);
                break;
            case 12:
                holder.rank.setImageResource(R.drawable.rank12);
                break;
            case 13:
                holder.rank.setImageResource(R.drawable.rank13);
                break;
            case 14:
                holder.rank.setImageResource(R.drawable.rank14);
                break;
            case 15:
                holder.rank.setImageResource(R.drawable.rank15);
                break;
            case 16:
                holder.rank.setImageResource(R.drawable.rank16);
                break;
            default:
                holder.rank.setImageResource(R.drawable.rank1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecordArrayList.size();
    }

    static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView score;
        TextView rating;
        ImageView rank;

        public RecordViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.record_layout_userName);
            score = itemView.findViewById(R.id.record_layout_score);
            rating = itemView.findViewById(R.id.record_layout_rating);
            rank = itemView.findViewById(R.id.record_layout_rank);
        }
    }
}

package com.egoregorov.colourmemory.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.database.DatabaseMethods;
import com.egoregorov.colourmemory.database.Record;
import com.egoregorov.colourmemory.services.IPostResponse;
import com.egoregorov.colourmemory.services.NetworkRequests;

/**
 * Created by Egor on 05.12.2017.
 */

public class CongratulationsDialogFragment extends DialogFragment implements IPostResponse {
    private static final String TAG = "CongratulationsDialogFr";

    private EditText mEnterYourNameEditText;
    private Button mOkButton;
    private Button mSkipButton;
    private TextView mFinalScoreTextView;
    private View mUserInputLayout;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_congatulations, null);
        mEnterYourNameEditText = v.findViewById(R.id.dialog_congratulations_enterYourName);
        mOkButton = v.findViewById(R.id.dialog_congratulations_ok);
        mSkipButton = v.findViewById(R.id.dialog_congratulations_skip);
        mFinalScoreTextView = v.findViewById(R.id.dialog_congratulations_finalScore);
        mUserInputLayout = v.findViewById(R.id.dialog_congratulations_userInputLayout);
        mProgressBar = v.findViewById(R.id.dialog_congratulations_loading);

        if (getArguments() != null){
            int finalScore = getArguments().getInt("FINAL_SCORE");
            mFinalScoreTextView.setText(String.valueOf(finalScore));
        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starts");
                if (!TextUtils.isEmpty(mEnterYourNameEditText.getText())){
                    Record record = new Record(mEnterYourNameEditText.getText().toString(),Integer.parseInt(mFinalScoreTextView.getText().toString()));
                    DatabaseMethods.saveRecord(record);

                    if (isOnline()){
                        mUserInputLayout.setVisibility(View.INVISIBLE);
                        mUserInputLayout.setClickable(false);
                        mProgressBar.setVisibility(View.VISIBLE);
                        NetworkRequests networkRequests = new NetworkRequests();
                        networkRequests.postRecord(CongratulationsDialogFragment.this,new Record(mEnterYourNameEditText.getText().toString(),Integer.valueOf(mFinalScoreTextView.getText().toString())));
                    } else {
                        Toast.makeText(getContext(), "Error saving data. No internet connection", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                } else {
                    mEnterYourNameEditText.setError("Can not be empty");
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(false);

        return v;
    }

    @Override
    public void succesfulPost() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Succesfully saved",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

    }

    @Override
    public void error() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"Error saving data",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

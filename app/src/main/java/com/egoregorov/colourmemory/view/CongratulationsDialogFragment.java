package com.egoregorov.colourmemory.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
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
import com.egoregorov.colourmemory.services.NetworkService;


public class CongratulationsDialogFragment extends DialogFragment implements IPostResponse {

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
        getDialog().setCanceledOnTouchOutside(false);

        if (getArguments() != null) {
            int finalScore = getArguments().getInt("FINAL_SCORE");
            mFinalScoreTextView.setText(String.valueOf(finalScore));
        }

        mOkButton.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(mEnterYourNameEditText.getText())) {
                if (isOnline()) {
                    sendRecordToServer();
                } else {
                    saveRecordLocally();
                }
            } else {
                mEnterYourNameEditText.setError("Can not be empty");
            }
        });

        mSkipButton.setOnClickListener(view -> dismiss());

        return v;
    }

    @Override
    public void successfulPost() {
        getActivity().runOnUiThread(() -> {
            Record record = new Record(mEnterYourNameEditText.getText().toString(), Integer.parseInt(mFinalScoreTextView.getText().toString()));
            record.setSavedOnServer(true);
            DatabaseMethods.saveRecord(record);
            Toast.makeText(getContext(), "Succesfully saved", Toast.LENGTH_LONG).show();
            dismiss();
        });

    }

    @Override
    public void error() {
        getActivity().runOnUiThread(() -> {
            Record record = new Record(mEnterYourNameEditText.getText().toString(), Integer.parseInt(mFinalScoreTextView.getText().toString()));
            record.setSavedOnServer(false);
            DatabaseMethods.saveRecord(record);
            Toast.makeText(getContext(), "Error saving data", Toast.LENGTH_LONG).show();
            dismiss();
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null){
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }

    private void sendRecordToServer() {
        mUserInputLayout.setVisibility(View.INVISIBLE);
        mUserInputLayout.setClickable(false);
        mProgressBar.setVisibility(View.VISIBLE);
        NetworkService networkService = new NetworkService();
        networkService.postRecord(CongratulationsDialogFragment.this, new Record(mEnterYourNameEditText.getText().toString(), Integer.valueOf(mFinalScoreTextView.getText().toString())));
    }

    private void saveRecordLocally() {
        Record record = new Record(mEnterYourNameEditText.getText().toString(), Integer.parseInt(mFinalScoreTextView.getText().toString()));
        record.setSavedOnServer(false);
        DatabaseMethods.saveRecord(record);
        Toast.makeText(getContext(), "Error saving data. No internet connection", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

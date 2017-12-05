package com.egoregorov.colourmemory.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.database.DatabaseMethods;
import com.egoregorov.colourmemory.database.Record;

/**
 * Created by Egor on 05.12.2017.
 */

public class CongratulationsDialogFragment extends DialogFragment {
    private static final String TAG = "CongratulationsDialogFr";

    private EditText mEnterYourNameEditText;
    private Button mOkButton;
    private Button mSkipButton;
    private TextView mFinalScoreTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_congatulations, null);
        mEnterYourNameEditText = v.findViewById(R.id.dialog_congratulations_enterYourName);
        mOkButton = v.findViewById(R.id.dialog_congratulations_ok);
        mSkipButton = v.findViewById(R.id.dialog_congratulations_skip);
        mFinalScoreTextView = v.findViewById(R.id.dialog_congratulations_finalScore);

        if (getArguments() != null){
            int finalScore = getArguments().getInt("FINAL_SCORE");
            mFinalScoreTextView.setText(String.valueOf(finalScore));
        }

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: starts");
                if (!TextUtils.isEmpty(mEnterYourNameEditText.getText())){
                    //TODO save score
                    Record record = new Record(mEnterYourNameEditText.getText().toString(),Integer.parseInt(mFinalScoreTextView.getText().toString()));
                    DatabaseMethods.saveRecord(record);
                    dismiss();
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

}

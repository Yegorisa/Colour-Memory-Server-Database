package com.egoregorov.colourmemory.view;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.model.Card;
import com.egoregorov.colourmemory.presenter.BoardPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements IBoardView {
    private static final String TAG = "MainActivityFragment";

    private View mView;
    private GridLayout mBoardLayout;
    private BoardPresenter mBoardPresenter = new BoardPresenter(this);
    private boolean mOneSelected = false;
    private TextView mScore;
    private boolean mBoardEnabled;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mBoardLayout = mView.findViewById(R.id.fragment_main_board);
        for (int i = 0; i < 16; i++) {
            mBoardLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setClickable(false);
                    if (mBoardEnabled) {
                        int position = mBoardLayout.indexOfChild(view);
                        Card card = mBoardPresenter.onCardSelected(position);

                        if (card != null) {
                            final ImageView iv = (ImageView) view;
                            if (mOneSelected) {
                                Log.d(TAG, "onClick: starts");
                                mBoardEnabled = false;
                                mOneSelected = false;
                            } else {
                                mOneSelected = true;
                            }
                            startCardShowAnimation(iv, card.getImageResourceId());
                        }
                    }
                }
            });
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScore = getActivity().findViewById(R.id.activity_main_score);
        mBoardPresenter.onCreate();
        mBoardPresenter.checkIfAllRecordsAreUpToDate();
    }


    @Override
    public void startNewGame() {
        mScore.setText("0");
        mBoardEnabled = true;
        mOneSelected = false;

        resetAllCards();
    }

    @Override
    public void lostScore(int position1, int position2, int currentScore) {
        ImageView card;
        card = (ImageView) mBoardLayout.getChildAt(position1);
        if (card != null) {
            card.setClickable(true);
            startCardCloseAnimation(card);
            card = (ImageView) mBoardLayout.getChildAt(position2);
            if (card != null) {
                card.setClickable(true);
                startCardCloseAnimation(card);
                mBoardEnabled = true;
                mScore.setText(String.valueOf(currentScore));
            }

        }


    }

    @Override
    public void gotTheScore(int position1, int position2, int currentScore) {
        ImageView card = (ImageView) mBoardLayout.getChildAt(position1);
        if (card != null) {

            card.setVisibility(View.INVISIBLE);
            card.setClickable(false);
            card = (ImageView) mBoardLayout.getChildAt(position2);
            if (card != null) {

                card.setVisibility(View.INVISIBLE);
                card.setClickable(false);
                mBoardEnabled = true;
                mScore.setText(String.valueOf(currentScore));
            }
        }


    }

    @Override
    public void gameCompleted(int finalScore) {
        DialogFragment dialogFragment = new CongratulationsDialogFragment();
        Bundle args = new Bundle();
        args.putInt("FINAL_SCORE", finalScore);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "CONGRATULATIONS_DIALOG");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_restart) {
            mBoardPresenter.onCreate();
            return true;
        }
        return false;
    }

    private void resetAllCards() {
        for (int i = 0; i < 16; i++) {
            ImageView imageView = (ImageView) mBoardLayout.getChildAt(i);
            imageView.setVisibility(View.VISIBLE);
            startCardCloseAnimation(imageView);
            imageView.setClickable(true);
        }
    }

    private void startCardShowAnimation(ImageView imageView, int imageResourceId) {
        if (imageView != null) {

            imageView.setRotationY(0f);
            imageView.animate().rotationY(-90f).setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageView.setImageResource(imageResourceId);
                    imageView.setRotationY(-270f);
                    imageView.animate().rotationY(-360f).setListener(null);

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
        }
    }

    private void startCardCloseAnimation(ImageView imageView) {
        if (imageView != null) {
            imageView.setRotationY(0f);
            imageView.animate().rotationY(90f).setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageView.setImageResource(R.drawable.card_bg);
                    imageView.setRotationY(270f);
                    imageView.animate().rotationY(360f).setListener(null);

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
        }

    }
}
